package Connection;

import com.fazecast.jSerialComm.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class arduinoConnection {

    public static void main(String[] args) throws FileNotFoundException {
        FileOutputStream out = new FileOutputStream("output.txt");
        SerialPort comPort = SerialPort.getCommPorts()[0];
        comPort.openPort();
        //comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
        InputStream in = comPort.getInputStream();
        char test[] = new char[10];
        try {
            while (true) {
                //for (int j = 0; j < 10; ++j)
                //System.out.print((char) in.read());
                out.write((char) in.read());
                //test[j]=(char)in.read(); 
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        comPort.closePort();

    }
}
