package subsistemaGestionPacientes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import elementos.Alarma;
import elementos.Estadistico;
import elementos.Medida;
import elementos.Paciente;

public class DatosPacientes implements Itf6_DatosPacientes {
	private HashMap<String, Paciente> pacientes;

	public DatosPacientes() {
		pacientes = new HashMap<>();
		leerDatosFicheroPacientes();

	}
	public void crearFicheros(String nss) {
		FileWriter fichero = null;
		PrintWriter pw = null;
		File miDir = new File(".");
        String path = miDir.getAbsolutePath().substring(0, miDir.getAbsolutePath().length() - 1);
        File directorio = new File(path + "\\ficheros\\"+ nss + "\\");
        directorio.mkdirs();
        try {
			fichero = new FileWriter(".\\ficheros\\"+ nss + "\\Estadistico.csv",true);
			fichero = new FileWriter(".\\ficheros\\"+ nss + "\\Alarmas.csv",true);
			fichero = new FileWriter(".\\ficheros\\"+ nss + "\\datosSimulados.csv",true);
			fichero = new FileWriter(".\\ficheros\\"+ nss + "\\Medidas.csv",true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public boolean darAlta(Paciente paciente) {
		if (pacientes.containsKey(paciente.getnSeguridadSocial()))
			return false;
		pacientes.put(paciente.getnSeguridadSocial(), paciente);
		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter(".\\ficheros\\registro.csv", true);
			pw = new PrintWriter(fichero);
			pw.println(paciente.getnSeguridadSocial() + ";" + paciente.getNombreAp() + ";" + paciente.getDireccion()
					+ ";" + paciente.getFechaNacimiento());
			this.crearFicheros(paciente.getnSeguridadSocial());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fichero)
					fichero.close();
				return true;
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean actualizar(Paciente paciente) {
		if (!pacientes.containsKey(paciente.getnSeguridadSocial()))
			return false;
		String nss = paciente.getnSeguridadSocial();
		pacientes.replace(nss, pacientes.get(nss), paciente);
		guardarDatosFicheroPacientes();
		return true;
	}

	@Override
	public boolean eliminar(Paciente paciente) {
		if (!pacientes.containsKey(paciente.getnSeguridadSocial()))
			return false;
		pacientes.remove(paciente.getnSeguridadSocial());
		guardarDatosFicheroPacientes();
		return true;
	}

	@Override
	public Paciente consultarDatosPaciente(String NSS) {
		if (pacientes.containsKey(NSS))
			return pacientes.get(NSS);
		else
			return null;
	}
	
	public boolean leerDatosFicheroPacientes() {
		BufferedReader br = null;
		try {
			new FileWriter(".\\ficheros\\registro.csv",true);
			br = new BufferedReader(new FileReader(".\\ficheros\\registro.csv"));
			String line;
			while((line = br.readLine())!=null) {
				if (line.split(";").length==4) {
					String[] fields = line.split(";");
					String nss = fields[0];
					if(nss.charAt(0)=='ï') {
						nss = nss.substring(3);
					}	
					String nombre = fields[1];	
					String direccion = fields[2];
					String fechaNacimiento = fields[3];
					pacientes.put(nss, new Paciente(nss, direccion, nombre, fechaNacimiento,
							new ArrayList<Estadistico>(), new ArrayList<Alarma>(), new ArrayList<Medida>()));
				}
				
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error leyendo archivo: " + e.getMessage());
		}
		return false;
	}

	public boolean guardarDatosFicheroPacientes() {
		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter(".\\ficheros\\registro.csv");
			pw = new PrintWriter(fichero);
			for (Map.Entry<String, Paciente> entry : pacientes.entrySet()) {
				pw.println(entry.getKey() + ";" + entry.getValue().getNombreAp() + ";" + entry.getValue().getDireccion()
						+ ";" + entry.getValue().getFechaNacimiento());
			}
			pw.close();
			return true;
		} catch (Exception e) {

		}
		return false;
	}

}