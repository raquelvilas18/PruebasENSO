package subsistemaSensorizacion;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;

import subsistemaAlmacenDatos.*;

public class DatosSensores {
	public DatosSensores() {

	}
	public void simularDatosSensores(int numeroDatosASimular, String nss) {
		FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
        	File miDir = new File(".");
            String path = miDir.getAbsolutePath().substring(0, miDir.getAbsolutePath().length() - 1);
            File directorio = new File(path + "\\ficheros\\"+ nss + "\\");
            directorio.mkdirs();
            fichero = new FileWriter(".\\ficheros\\"+ nss + "\\datosSimulados.csv");
            pw = new PrintWriter(fichero);
            DecimalFormatSymbols separador = new DecimalFormatSymbols();
            separador.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("#.#", separador);
            for (int i = 0; i < numeroDatosASimular; i++){
                Double temp = 34 + Math.random()*8;
                int pulso = (int) Math.floor(Math.random()*(180)+40);
                Date myDate = new Date();
                String fecha = new SimpleDateFormat("dd-MM-yyyy;kk:mm:ss").format(myDate);
                pw.println(fecha+";"+df.format(temp)+";"+pulso);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
	}
}
