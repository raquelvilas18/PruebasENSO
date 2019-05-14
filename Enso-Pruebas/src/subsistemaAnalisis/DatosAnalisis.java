package subsistemaAnalisis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import elementos.Alarma;
import elementos.Estadistico;
import elementos.Medida;
import elementos.Paciente;
import subsistemaAlmacenDatos.ExcepcionDeFechas;
import subsistemaGestionPacientes.*;

public class DatosAnalisis implements Itf2_DatosInstantaneos, Itf4_Estadisticas, Itf5_Alarmas {
	private Itf6_DatosPacientes iDatosPacientes;

	public DatosAnalisis(DatosPacientes datosPacientes) {
		this.iDatosPacientes = (Itf6_DatosPacientes) datosPacientes;
	}

	@Override
	public ArrayList<Alarma> verAlarmas(Paciente p) {
		Paciente paciente = this.iDatosPacientes.consultarDatosPaciente(p.getnSeguridadSocial());
		if (paciente.getAlarmas().isEmpty() || paciente.getAlarmas() == null) {
			paciente.setAlarmas(this.leerDatosFicheroAlarmas(paciente));
		}
		return paciente.getAlarmas();
	}

	public ArrayList<Alarma> leerDatosFicheroAlarmas(Paciente paciente) {
		BufferedReader br = null;
		ArrayList<Alarma> alarmas = new ArrayList<>();
		try {
			br = new BufferedReader(new FileReader(".\\ficheros\\" + paciente.getnSeguridadSocial() + "\\Alarmas.csv"));
			String line;
			while((line = br.readLine())!=null) {
				String[] fields = line.split(";");
				alarmas.add(new Alarma(fields[0] + ";" + fields[1], fields[2], Float.parseFloat(fields[3])));
			}
			br.close();
		} catch (Exception e) {
			System.out.println("Error leyendo archivo alarmas: " + e.getMessage());
		}
		return alarmas;
	}

	public ArrayList<Estadistico> leerDatosFicheroEstadisticos(Paciente paciente) {
		BufferedReader br = null;
		ArrayList<Estadistico> estadisticos = new ArrayList<>();
		try {
			br =new BufferedReader(new FileReader(".\\ficheros\\" + paciente.getnSeguridadSocial() + "\\Estadistico.csv"));
			String line;
			while((line = br.readLine())!=null) {
			    String [] fields = line.split(";");
			    HashMap<String,Float> datos = new HashMap<>();
			    if(fields[2] ==  "T") {
					datos.put("t_maximo", Float.parseFloat(fields[3]));
					datos.put("t_minimo", Float.parseFloat(fields[4]));
					datos.put("t_media", Float.parseFloat(fields[5]));
					datos.put("t_primerCuartil", Float.parseFloat(fields[6]));
					datos.put("t_segundoCuartil", Float.parseFloat(fields[7]));
					datos.put("t_tercerCuartil", Float.parseFloat(fields[8]));
					datos.put("t_nAlarmas", Float.parseFloat(fields[9]));
				} else if (fields[2] == "F") {
					datos.put("f_maximo", Float.parseFloat(fields[3]));
					datos.put("f_minimo", Float.parseFloat(fields[4]));
					datos.put("f_media", Float.parseFloat(fields[5]));
					datos.put("f_primerCuartil", Float.parseFloat(fields[6]));
					datos.put("f_segundoCuartil", Float.parseFloat(fields[7]));
					datos.put("f_tercerCuartil", Float.parseFloat(fields[8]));
					datos.put("f_nAlarmas", Float.parseFloat(fields[9]));
				}
			    
	    	estadisticos.add(new Estadistico(datos,fields[0] + ";" + fields[1]));

			}
	    	br.close();
		} catch (Exception e) {
		  System.out.println("Error leyendo archivo estadisticos: " + e.getMessage());
		} 
		return estadisticos;
	}

	@Override
	public ArrayList<Estadistico> solicitarEstadisticos(Paciente paciente, String fechaInicio, String fechaFin) {
		Paciente p = iDatosPacientes.consultarDatosPaciente(paciente.getnSeguridadSocial());
		ArrayList<Estadistico> estadisticos = new ArrayList<>();

		try {
			Calendar cInicio, cFin, c;
			cInicio = transformarFecha(fechaInicio);
			cFin = transformarFecha(fechaFin);

			if (cInicio.after(cFin))
				throw new ExcepcionDeFechas("Fecha de fin anterior a la fecha de inicio");
			if(p.getEstadisticos().isEmpty() || p.getEstadisticos() == null) {
				p.setEstadisticos(this.leerDatosFicheroEstadisticos(paciente));
			}
			
			for (Estadistico e : p.getEstadisticos()) {
				System.out.println("jeje");
				c = transformarFecha(e.getFecha());
				if (c.after(cInicio) && c.before(cFin))
					estadisticos.add(e);
			}
			
		} catch (ExcepcionDeFechas e) {
			System.out.println(e);
		}
		return estadisticos;
	}

	@Override
	public Alarma generarAlarma(Medida medida, Paciente paciente) {
		Double umbralSuperiorF = (220 - obtenerEdad(paciente.getFechaNacimiento())) * 0.8;
		Alarma a = null;
		String url = ".\\ficheros\\" + paciente.getnSeguridadSocial();

		if (medida.getTemperatura() < 36.0 || medida.getTemperatura() > 38.0) {
			a = new Alarma(medida.getFecha(), "T", medida.getTemperatura());
			paciente.getAlarmas().add(a);
			guardarDatosFicheroAlertas(a, url);
		}
		if (medida.getRitmo() < 50.0 || medida.getRitmo() > umbralSuperiorF) {
			a = new Alarma(medida.getFecha(), "F", medida.getRitmo());
			paciente.getAlarmas().add(a);
			guardarDatosFicheroAlertas(a, url);
		}
		return a;
	}

	public void guardarDatosFicheroAlertas(Alarma a, String url) {
		try {
			File miDir = new File(".");
	        String path = miDir.getAbsolutePath().substring(0, miDir.getAbsolutePath().length() - 1);
	        File directorio = new File(path + url);
	        directorio.mkdirs();
	        FileWriter fichero = new FileWriter(url + "\\Alarmas.csv",true);
	        PrintWriter pw = new PrintWriter(fichero);
				
			pw.write(a.getFecha() + ";" + a.getTipo() + ";" + a.getValor() + "\n");
			pw.close();
		} catch (IOException e) {
			System.out.println("Error en el guardado de la alarma: " + e);
		}
	}

	public void guardarDatosFicheroEstadisticos(Estadistico e, String url) {
		try {
			File miDir = new File(".");
	        String path = miDir.getAbsolutePath().substring(0, miDir.getAbsolutePath().length() - 1);
	        File directorio = new File(path + url);
	        directorio.mkdirs();
	        FileWriter fichero = new FileWriter(url + "\\Estadistico.csv",true);
	        PrintWriter pw = new PrintWriter(fichero);
			HashMap<String, Float> d = e.getDatos();
			
			if(d.containsKey("t_maximo"))
				pw.write(e.getFecha() + ";T;" + d.get("t_maximo") + ";" + d.get("t_minimo") + ";" + d.get("t_media")
						+ ";" + d.get("t_primerCuartil") + ";" + d.get("t_segundoCuartil") + ";" + d.get("t_tercerCuartil")
						+ ";" + d.get("t_nAlarmas") + "\n");
			else if(d.containsKey("f_maximo"))
				pw.write(e.getFecha() + ";F;" + d.get("f_maximo") + ";" + d.get("f_minimo") + ";" + d.get("f_media")
				+ ";" + d.get("f_primerCuartil") + ";" + d.get("f_segundoCuartil") + ";" + d.get("f_tercerCuartil")
				+ ";" + d.get("f_nAlarmas") + "\n");
			pw.close();
		} catch (IOException excp) {
			System.out.println("Error en el guardado de estadisticos");
		}
	}

	@Override
	public Estadistico generarEstadistico(Paciente paciente) {
		HashMap<String, Float> datosT = new HashMap<>();
		HashMap<String, Float> datosF = new HashMap<>();
		String url = "ficheros\\" + paciente.getnSeguridadSocial();
		Estadistico e = null;

		Date date = new Date();

		String strDateFormat = "dd-MM-yyyy;kk:mm:ss";
		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);

		String fechaCompleta = dateFormat.format(date);

		ArrayList<Medida> medidas = new ArrayList<>();

		Calendar cMedida, cInicio, cFin;
		cInicio = transformarFecha(fechaCompleta);
		cInicio.set(Calendar.MINUTE, 0);
		cInicio.set(Calendar.SECOND, 0);
		
		cFin = transformarFecha(fechaCompleta);
		cFin.add(Calendar.HOUR, 1);
		cFin.set(Calendar.MINUTE, 0);
		cFin.set(Calendar.SECOND, 0);

		for (Medida m : paciente.getMedidas()) {
			cMedida = transformarFecha(m.getFecha());
			if (cMedida.after(cInicio) && cMedida.before(cFin))
				medidas.add(m);
		}

		if(!medidas.isEmpty()) {
			datosT.put("t_maximo", maximoTemperatura(medidas));
			datosT.put("t_minimo", minimoTemperatura(medidas));
			datosT.put("t_media", mediaTemperatura(medidas));
			Float[] cuartiles = cuartilesTemperatura(medidas);
			datosT.put("t_primerCuartil", cuartiles[0]);
			datosT.put("t_segundoCuartil", cuartiles[1]);
			datosT.put("t_tercerCuartil", cuartiles[2]);
			datosT.put("t_nAlarmas", nAlarmasTemperatura(cInicio, cFin, paciente));
			e = new Estadistico(datosT, fechaCompleta);

			this.guardarDatosFicheroEstadisticos(e, url);
			
			datosF.put("f_maximo", maximoFrecuencia(medidas));
			datosF.put("f_minimo", minimoFrecuencia(medidas));
			datosF.put("f_media", mediaFrecuencia(medidas));
			cuartiles = cuartilesFrecuencia(medidas);
			datosF.put("f_primerCuartil", cuartiles[0]);
			datosF.put("f_segundoCuartil", cuartiles[1]);
			datosF.put("f_tercerCuartil", cuartiles[2]);
			datosF.put("f_nAlarmas", nAlarmasFrecuencia(cInicio, cFin, paciente));
	
			e = new Estadistico(datosF, fechaCompleta);
	
			this.guardarDatosFicheroEstadisticos(e, url);
		} else {
			System.out.println("No hay medidas con las que realizar el estadistico");
		}
		return e;
	}

	private Float nAlarmasTemperatura(Calendar cInicio, Calendar cFin, Paciente paciente) {
		Float numero = 0.0f;
		for (Alarma a : this.verAlarmas(paciente)) {
			Calendar cAlarma = transformarFecha(a.getFecha());
			if (a.getTipo().equals("T") && cAlarma.after(cInicio) && cAlarma.before(cFin))
				numero += 1.0f;
		}
		return numero;
	}

	private Float maximoFrecuencia(ArrayList<Medida> medidas) {
		Float maximo = medidas.get(0).getRitmo();
		for (Medida m : medidas) {
			if (m.getRitmo() > maximo)
				maximo = m.getRitmo();
		}
		return maximo;
	}

	private Float minimoFrecuencia(ArrayList<Medida> medidas) {
		Float minimo = medidas.get(0).getRitmo();
		for (Medida m : medidas) {
			if (m.getRitmo() < minimo)
				minimo = m.getRitmo();
		}
		return minimo;
	}

	private Float mediaFrecuencia(ArrayList<Medida> medidas) {
		Float suma = 0.0f;
		for (Medida m : medidas) {
			suma += m.getRitmo();
		}
		return suma / medidas.size();
	}

	private Float[] cuartilesFrecuencia(ArrayList<Medida> medidas) {
		Float[] medidasF = new Float[medidas.size()];
		Float[] cuartiles = new Float[3];
		for (int i = 0; i < medidas.size(); i++) {
			medidasF[i] = medidas.get(i).getRitmo();
		}
		cuartiles[0] = (float) Math.round(medidasF.length * 25 / 100);
		cuartiles[1] = (float) Math.round(medidasF.length * 50 / 100);
		cuartiles[2] = (float) Math.round(medidasF.length * 75 / 100);

		return cuartiles;
	}

	private Float nAlarmasFrecuencia(Calendar cInicio, Calendar cFin, Paciente p) {
		Float numero = 0.0f;
		for (Alarma a : this.verAlarmas(p)) {
			Calendar cAlarma = transformarFecha(a.getFecha());
			if (a.getTipo().equals("F") && cAlarma.after(cInicio) && cAlarma.before(cFin))
				numero += 1.0f;
		}
		return numero;
	}

	private Float[] cuartilesTemperatura(ArrayList<Medida> medidas) {
		Float[] medidasF = new Float[medidas.size()];
		Float[] cuartiles = new Float[3];
		for (int i = 0; i < medidas.size(); i++) {
			medidasF[i] = medidas.get(i).getTemperatura();
		}

		cuartiles[0] = (float) Math.round(medidasF.length * 25 / 100);
		cuartiles[1] = (float) Math.round(medidasF.length * 50 / 100);
		cuartiles[2] = (float) Math.round(medidasF.length * 75 / 100);

		return cuartiles;
	}

	private Float minimoTemperatura(ArrayList<Medida> medidas) {
		Float minimo = medidas.get(0).getTemperatura();
		for (Medida m : medidas) {
			if (m.getRitmo() < minimo)
				minimo = m.getRitmo();
		}
		return minimo;
	}

	private Float maximoTemperatura(ArrayList<Medida> medidas) {
		Float maximo = medidas.get(0).getTemperatura();
		for (Medida m : medidas) {
			if (m.getRitmo() > maximo)
				maximo = m.getRitmo();
		}
		return maximo;
	}

	private Float mediaTemperatura(ArrayList<Medida> medidas) {
		Float suma = 0.0f;
		for (Medida m : medidas) {
			suma += m.getTemperatura();
		}
		return suma / medidas.size();
	}

	private Calendar transformarFecha(String fecha) {
		Calendar calendario = Calendar.getInstance();
		try {
			String[] arrayFecha = fecha.split(";");
			String[] arrayDia = arrayFecha[0].split("-");
			String[] arrayHora = arrayFecha[1].split(":");
			calendario.set(Integer.parseInt(arrayDia[2]), Integer.parseInt(arrayDia[1]), Integer.parseInt(arrayDia[0]),
					Integer.parseInt(arrayHora[0]), Integer.parseInt(arrayHora[1]), Integer.parseInt(arrayHora[2]));
		} catch (NumberFormatException e) {
			System.out.println("Error en el formato de la fecha");
		}
		return calendario;
	}

	private Integer obtenerEdad(String fNacimiento) {
		Integer edad = 0;

		Date date = new Date();
		String strDateFormat = "dd-MM-yyyy";
		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
		String fActual = dateFormat.format(date);
		
		String[] fechaNacimiento = fNacimiento.split(";");
		String[] arrayFecha = fechaNacimiento[0].split("-");
		String[] arrayHoy = fActual.split("-");
		
		try {
			Integer anoNacimiento = Integer.parseInt(arrayFecha[2]);
			Integer anoActual = Integer.parseInt(arrayHoy[2]);

			edad = anoActual - anoNacimiento;
			if (Integer.parseInt(arrayFecha[1]) >= Integer.parseInt(arrayHoy[1])
					&& Integer.parseInt(arrayFecha[0]) > Integer.parseInt(arrayHoy[0]))
				edad--;
		} catch (NumberFormatException e) {
			System.out.println("El formato de la fecha no es valido en la obtencion de la edad");
		}
		return edad;
	}
}
