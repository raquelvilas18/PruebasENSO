package subsistemaAlmacenDatos;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import elementos.Medida;
import elementos.Paciente;
import subsistemaAnalisis.DatosAnalisis;
import subsistemaAnalisis.Itf2_DatosInstantaneos;
import subsistemaGestionPacientes.DatosPacientes;
import subsistemaGestionPacientes.Itf6_DatosPacientes;

public class DatosMedidas implements Itf1_DatosSimulados, Itf3_SeriesTemporales {
	private Itf6_DatosPacientes itfDatosPacientes;
	public DatosMedidas(DatosPacientes datospacientes) {
		itfDatosPacientes =(Itf6_DatosPacientes) datospacientes;
	}
	public void setDatosPacientes(DatosPacientes datospacientes) {
		itfDatosPacientes =(Itf6_DatosPacientes) datospacientes;
	}	
	
	//datos simulados
	@Override
	public String leerDatos(String URLArchivo, ArrayList<Medida> medidas) {
		String pattern = Pattern.quote(System.getProperty("file.separator"));
		String [] urlFields = URLArchivo.split(pattern);
		String usuario = urlFields[2];
		BufferedReader br = null;
	      try {
	         br =new BufferedReader(new FileReader(URLArchivo));
	         String line = br.readLine();
	         while (null!=line) {
	            String [] fields = line.split(";");
	            String fecha = fields[0];
	            String hora = fields[1];
	            fecha = fecha + ";" + hora;
	            Float temperatura = Float.parseFloat(fields[2]);  
	            Float ritmo = Float.parseFloat(fields[3]);  
	            medidas.add(new Medida(temperatura, ritmo, fecha));
	            String [] fields2 = hora.split(":");
	            line = br.readLine();
	         }
	         this.guardarFicheroMedidas(medidas,usuario);
	         br.close();
	      } catch (Exception e) {
	    	  System.out.println("Error leyendo archivo datos: " + e.getMessage());
	      }
		return usuario;
	}
	
	private void guardarFicheroMedidas(ArrayList<Medida> medidas, String nss) {
		File miDir = new File(".");
        String path = miDir.getAbsolutePath().substring(0, miDir.getAbsolutePath().length() - 1);
        File directorio = new File(path + ".\\ficheros\\" + nss);
        directorio.mkdirs();
        try {
        	FileWriter fichero = new FileWriter(".\\ficheros\\" + nss + "\\Medidas.csv");
	        PrintWriter pw = new PrintWriter(fichero);

	        for(Medida m : medidas) {
	        	pw.write(m.getFecha() + ";" + m.getTemperatura() + ";" + m.getRitmo() + "\n");
	        }
			pw.close();

	    } catch (Exception e) {
	  	  System.out.println("Error leyendo archivo medidas: " + e.getMessage());
	    }
	}
	
	//Asignar medidas al paciente en memoria
	@Override
	public boolean enviarDatos(ArrayList<Medida> medidas, String NSS) {
		// TODO
		Paciente p = itfDatosPacientes.consultarDatosPaciente(NSS);
		ArrayList<Medida> meds= p.getMedidas();
		Itf2_DatosInstantaneos datos =  new DatosAnalisis((DatosPacientes)itfDatosPacientes);
		
		for(Medida m : medidas) {
			meds.add(m);
			String fecha = m.getFecha();
			String split[] = fecha.split(";");
			String hora[] = split[1].split(":");
			if(hora[1].equals("00")) {
				datos.generarEstadistico(p);
			}
			datos.generarAlarma(m, p);
		}
		itfDatosPacientes.consultarDatosPaciente(NSS).setMedidas(meds);
		
		return true;
	}

	//series temporales
	@Override
	public ArrayList<Medida> solicitarMedidas(Paciente aux, String fechaInicio, String fechaFin) {
		ArrayList<Medida> medidas = new ArrayList();
		Paciente paciente = this.itfDatosPacientes.consultarDatosPaciente(aux.getnSeguridadSocial());
		try {
			this.comprobarFechas(fechaInicio, fechaFin);//comprobamos posibles errores			
			String partesFechaInicio[] = fechaInicio.split(";");
			String camposFechaInicio[] = partesFechaInicio[0].split("-");
			String camposHoraInicio[] = partesFechaInicio[1].split(":");
			
			String partesFechaFin[] = fechaFin.split(";");
			String camposFechaFin[] = partesFechaFin[0].split("-");
			String camposHoraFin[] = partesFechaFin[1].split(":");
			
			@SuppressWarnings("deprecation")
			Date fechaIni = new Date(Integer.parseInt(camposFechaInicio[2]),Integer.parseInt(camposFechaInicio[1]),
					Integer.parseInt(camposFechaInicio[0]),Integer.parseInt(camposHoraInicio[0]),
					Integer.parseInt(camposHoraInicio[1]));
			
			@SuppressWarnings("deprecation")
			Date fechaF = new Date(Integer.parseInt(camposFechaFin[2]),Integer.parseInt(camposFechaFin[1]),
					Integer.parseInt(camposFechaFin[0]),Integer.parseInt(camposHoraFin[0]),
					Integer.parseInt(camposHoraFin[1]));
			
			for(int i=0; i<paciente.getMedidas().size();i++) {//
				Medida med = paciente.getMedidas().get(i);
				String campos[] = med.getFecha().split(";");
				String camposFecha[] = campos[0].split("-");
				String camposHora[] = campos[1].split(":");
				
				@SuppressWarnings("deprecation")
				Date fecha = new Date(Integer.parseInt(camposFecha[2]),Integer.parseInt(camposFecha[1]),
						Integer.parseInt(camposFecha[0]),Integer.parseInt(camposHora[0]),
						Integer.parseInt(camposHora[1]));
				
				if(fecha.after(fechaIni) && fechaF.after(fecha)) {
					medidas.add(med);
				}


			}
		} catch(ExcepcionDeFechas e) {
			System.out.println(e);
		}
		
		return medidas;
	}
	
	
	@Override
	public ArrayList<Medida> solicitarMedidas(Paciente paciente, String fechaInicio) {
		Paciente aux = this.itfDatosPacientes.consultarDatosPaciente(paciente.getnSeguridadSocial());
/*1*/	ArrayList<Medida> medidas = new ArrayList();
		try {

			String partesFechaInicio[] = fechaInicio.split(";");
/*2*/		if(partesFechaInicio.length<2){
/*3*/			throw new ExcepcionDeFechas("Error de formato");
			}
/*4*/		String camposFechaInicio[] = partesFechaInicio[0].split("-");
		
/*5*/		if(camposFechaInicio.length < 3) {
/*6*/			throw new ExcepcionDeFechas("Error de formato");
			}
			
/*7*/		String camposHoraInicio[] = partesFechaInicio[1].split(":");
			
/*8*/		if(camposHoraInicio.length < 2) {
/*9*/			throw new ExcepcionDeFechas("Error de formato");
			}
			//comprobamos que la fecha sea posterior a la actual
/*10*/		Date fechaActual = new Date();
			
			Date fechaIni = new Date(Integer.parseInt(camposFechaInicio[2])-1900,Integer.parseInt(camposFechaInicio[1])-1,
					Integer.parseInt(camposFechaInicio[0].trim()),Integer.parseInt(camposHoraInicio[0]),
					Integer.parseInt(camposHoraInicio[1]));
			
/*11*/		if(fechaIni.after(fechaActual)) {
/*12*/			throw new ExcepcionDeFechas("Fecha posterior a la actual");
			}
					
/*13*/		fechaIni = new Date(Integer.parseInt(camposFechaInicio[2]),Integer.parseInt(camposFechaInicio[1]),
					Integer.parseInt(camposFechaInicio[0]),Integer.parseInt(camposHoraInicio[0]),
					Integer.parseInt(camposHoraInicio[1]));
/*14*/		for(int i=0; i<aux.getMedidas().size(); i++) {
				Medida med = aux.getMedidas().get(i);
				String campos[] = med.getFecha().split(";");
				String camposFecha[] = campos[0].split("-");
				String camposHora[] = campos[1].split(":");
				@SuppressWarnings("deprecation")
				Date fecha = new Date(Integer.parseInt(camposFecha[2]),Integer.parseInt(camposFecha[1]),
						Integer.parseInt(camposFecha[0]),Integer.parseInt(camposHora[0]),
						Integer.parseInt(camposHora[1]));
				
/*15*/			if(fecha.after(fechaIni)) {
/*16*/				medidas.add(med);
				}

/*17*/		}
		} catch(ExcepcionDeFechas e) {
			System.out.println(e);
		}
		
		return medidas;
	}
	
	
	private void comprobarFechas(String fechaInicio) throws ExcepcionDeFechas{
		//primero comprobamos el formato

		String partesFechaInicio[] = fechaInicio.split(";");
		if(partesFechaInicio.length<2){
			throw new ExcepcionDeFechas("Error de formato");
		}
		String camposFechaInicio[] = partesFechaInicio[0].split("-");


		
		if(camposFechaInicio.length < 3) {
			throw new ExcepcionDeFechas("Error de formato");
		}
		
		String camposHoraInicio[] = partesFechaInicio[1].split(":");
		
		if(camposHoraInicio.length < 2) {
			throw new ExcepcionDeFechas("Error de formato");
		}
		//comprobamos que la fecha sea posterior a la actual
		Date fechaActual = new Date();

		@SuppressWarnings("deprecation")
		Date fechaIni = new Date(Integer.parseInt(camposFechaInicio[2])-1900,Integer.parseInt(camposFechaInicio[1])-1,
				Integer.parseInt(camposFechaInicio[0].trim()),Integer.parseInt(camposHoraInicio[0]),
				Integer.parseInt(camposHoraInicio[1]));
		
		if(fechaIni.after(fechaActual)) {
			throw new ExcepcionDeFechas("Fecha posterior a la actual");
		}
	}
	
	private void comprobarFechas(String fechaInicio, String fechaFin) throws ExcepcionDeFechas{
		//comprobamos fechas individualmente
		this.comprobarFechas(fechaInicio);
		this.comprobarFechas(fechaFin);
		
		//comprobamos que Fin > Inicio
		String partesFechaInicio[] = fechaInicio.split(";");
		String camposFechaInicio[] = partesFechaInicio[0].split("-");
		String camposHoraInicio[] = partesFechaInicio[1].split(":");
		
		String partesFechaFin[] = fechaFin.split(";");
		String camposFechaFin[] = partesFechaFin[0].split("-");
		String camposHoraFin[] = partesFechaFin[1].split(":");
		
		@SuppressWarnings("deprecation")
		Date fechaIni = new Date(Integer.parseInt(camposFechaInicio[2])-1900,Integer.parseInt(camposFechaInicio[1])-1,
				Integer.parseInt(camposFechaInicio[0]),Integer.parseInt(camposHoraInicio[0]),
				Integer.parseInt(camposHoraInicio[1]));
		
		@SuppressWarnings("deprecation")
		Date fechaF = new Date(Integer.parseInt(camposFechaFin[2]) -1900,Integer.parseInt(camposFechaFin[1])-1,
				Integer.parseInt(camposFechaFin[0]),Integer.parseInt(camposHoraFin[0]),
				Integer.parseInt(camposHoraFin[1]));
		
		if(fechaIni.after(fechaF)) {
			throw new ExcepcionDeFechas("Fecha de inicio posterior a la de fin");
		}

	}
}
/*
@Override
public ArrayList<Medida> solicitarMedidas(Paciente paciente, String fechaInicio, String fechaFin) {
	Path currentRelativePath = Paths.get("");
	String ruta = currentRelativePath.toAbsolutePath().toString()+"ficheros/"+paciente.getnSeguridadSocial();
	ArrayList<Medida> medidas = new ArrayList();
	try {
		this.comprobarFechas(fechaInicio, fechaFin);//comprobamos posibles errores
		FileReader f = new FileReader(ruta);
		BufferedReader b= new BufferedReader(f);
		String entrada;
		
		String partesFechaInicio[] = fechaInicio.split(";");
		String camposFechaInicio[] = partesFechaInicio[0].split("-");
		String camposHoraInicio[] = partesFechaInicio[1].split("-");
		
		String partesFechaFin[] = fechaFin.split(";");
		String camposFechaFin[] = partesFechaFin[0].split("-");
		String camposHoraFin[] = partesFechaFin[1].split(":");
		
		@SuppressWarnings("deprecation")
		Date fechaIni = new Date(Integer.parseInt(camposFechaInicio[0]),Integer.parseInt(camposFechaInicio[1]),
				Integer.parseInt(camposFechaInicio[2]),Integer.parseInt(camposHoraInicio[0]),
				Integer.parseInt(camposHoraInicio[1]));
		
		@SuppressWarnings("deprecation")
		Date fechaF = new Date(Integer.parseInt(camposFechaFin[0]),Integer.parseInt(camposFechaFin[1]),
				Integer.parseInt(camposFechaFin[2]),Integer.parseInt(camposHoraFin[0]),
				Integer.parseInt(camposHoraFin[1]));
		
		while((entrada=b.readLine())!=null) {//lectura de fichero
			String campos[] = entrada.split(";");
			String camposFecha[] = campos[0].split("-");
			String camposHora[] = campos[1].split(":");
			
			@SuppressWarnings("deprecation")
			Date fecha = new Date(Integer.parseInt(camposFecha[0]),Integer.parseInt(camposFecha[1]),
					Integer.parseInt(camposFecha[2]),Integer.parseInt(camposHora[0]),
					Integer.parseInt(camposHora[1]));
			
			if(fecha.after(fechaIni) && fechaF.after(fecha)) {
				Medida med= new Medida(Float.parseFloat(campos[2]), Float.parseFloat(campos[3]), campos[0]+";"+campos[1]);
				medidas.add(med);
			}


		}
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch(ExcepcionDeFechas e) {
		System.out.println(e);
	}
	
	return medidas;
}
*/
/*
@Override
public ArrayList<Medida> solicitarMedidas(Paciente paciente, String fechaInicio) {
	
	Path currentRelativePath = Paths.get("");
	String ruta = currentRelativePath.toAbsolutePath().toString()+"ficheros/"+paciente.getnSeguridadSocial();
	ArrayList<Medida> medidas = new ArrayList();
	try {
		this.comprobarFechas(fechaInicio);//comprobamos posibles errores
		FileReader f = new FileReader(ruta);
		BufferedReader b= new BufferedReader(f);
		String entrada;
		String partesFechaInicio[] = fechaInicio.split(";");
		String camposFechaInicio[] = partesFechaInicio[0].split("-");
		String camposHoraInicio[] = partesFechaInicio[1].split("-");
		
		@SuppressWarnings("deprecation")
		Date fechaIni = new Date(Integer.parseInt(camposFechaInicio[0]),Integer.parseInt(camposFechaInicio[1]),
				Integer.parseInt(camposFechaInicio[2]),Integer.parseInt(camposHoraInicio[0]),
				Integer.parseInt(camposHoraInicio[1]));
		
		while((entrada=b.readLine())!=null) {//lectura de fichero
			String campos[] = entrada.split(";");
			String camposFecha[] = campos[0].split("-");
			String camposHora[] = campos[1].split(":");
			
			@SuppressWarnings("deprecation")
			Date fecha = new Date(Integer.parseInt(camposFecha[0]),Integer.parseInt(camposFecha[1]),
					Integer.parseInt(camposFecha[2]),Integer.parseInt(camposHora[0]),
					Integer.parseInt(camposHora[1]));
			
			if(fecha.after(fechaIni)) {
				Medida med= new Medida(Float.parseFloat(campos[2]), Float.parseFloat(campos[3]), campos[0]+";"+campos[1]);
				medidas.add(med);
			}

		}
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch(ExcepcionDeFechas e) {
		System.out.println(e);
	}
	
	return medidas;
}
*/