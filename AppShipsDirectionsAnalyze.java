package simulator;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage1;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisMessageException;
import dk.dma.ais.sentence.SentenceException;
import dk.dma.ais.sentence.Vdm;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class AppShipsDirectionsAnalyze {
    public static Map<Integer, Ship> shipsToEast = new HashMap<>();
    public static Map<Integer, Ship> shipsToSouth = new HashMap<>();
    public static File file = new File("D:\\2_Oldenburg\\20220101_AISHUB.txt");
    public static String pathToWriteFileToEast = "aisshipstoeast.txt";
    public static String pathToWriteFileToSouth = "aisshipstosouth.txt";
    public static Deque<String> list = new ArrayDeque<>();
    static int count = 0;
    static int catchIndex = 0;
    static String destination;


    public static void main(String[] args) {
        writeFile("START", pathToWriteFileToEast);
        writeFile("START", pathToWriteFileToSouth);
        readLines();
        writeFile("\nFINISH", pathToWriteFileToEast);
        writeFile("\nFINISH", pathToWriteFileToSouth);
    }



    private static void readLines() {
        try (FileInputStream inputStream = new FileInputStream(file);
             Scanner sc = new Scanner(inputStream, "UTF-8")) {
            AisMessage message = null;
            int check = 0;
            while (sc.hasNextLine()) {
                count++;
                check++;
                if (check == 10000000) {
                    writeFile("\n+++++++++++TO EAST++++++++++++" + "lines Checked" + count, pathToWriteFileToEast);
                    shipsToEast.values().forEach(s -> writeFile("\n" + s.toString(), pathToWriteFileToEast));
                    writeFile("\n+++++++++++TO SOUTH++++++++++++", pathToWriteFileToSouth);
                    shipsToSouth.values().forEach(s -> writeFile("\n" + s.toString(), pathToWriteFileToSouth));
                    check = 0;
                }
                String value = sc.nextLine();
                if (value.length() == 63) {
                    Vdm vdm = new Vdm();
                    try {
                        vdm.parse(value.substring(16));
                        message = AisMessage.getInstance(vdm);
                    } catch (SentenceException | SixbitException | AisMessageException e) {
                        continue;
                    }
                    if (message instanceof AisMessage1) {
                        AisMessage1 AISMSG1 = (AisMessage1) message;
                        int mmsiShip = AISMSG1.getUserId();
                        double latitudeDouble = AISMSG1.getPos().getLatitudeDouble();
                        double longitudeDouble = AISMSG1.getPos().getLongitudeDouble();
                        Point2D.Double point = new Point2D.Double(latitudeDouble, longitudeDouble);
//                        System.out.println(point);
                        if (BoundaryArea.insideArea(point, TssArea.trafficLineToEast)) {
                            Ship ship = new Ship();
                            ship.setLatid(latitudeDouble);
                            ship.setLongit(longitudeDouble);
                            ship.setMmsi(mmsiShip);
                            shipsToEast.put(mmsiShip, ship);
                        }
                        if (BoundaryArea.insideArea(point, TssArea.trafficLineToSouth)) {
                            Ship ship = new Ship();
                            ship.setLatid(latitudeDouble);
                            ship.setLongit(longitudeDouble);
                            ship.setMmsi(mmsiShip);
                            shipsToSouth.put(mmsiShip, ship);
                        }
                    }
                } else if (value.length() > 63) {
                    list.push(value);
                    catchIndex = count;
                } else {
                    if (count - catchIndex == 1) {
                        Vdm vdm = new Vdm();
                        try {
                            vdm.parse(list.pop().substring(16));
                            vdm.parse(value.substring(16));
                            message  = AisMessage.getInstance(vdm);
                        } catch (SentenceException | SixbitException | AisMessageException e) {
                            continue;
                        }
                        if (message instanceof AisMessage5) {
                            AisMessage5 AISMSG5 = (AisMessage5) message;
                            int mmsiShip = AISMSG5.getUserId();
                            if (shipsToEast.containsKey(mmsiShip)) {
                                destination = AISMSG5.getDest();
                                Ship ship = shipsToEast.get(mmsiShip);
                                ship.setDestin(destination);
                                shipsToEast.put(mmsiShip, ship);
//                                writeFile("\n" + ship, pathToWriteFileToEast);
                            }
                            if (shipsToSouth.containsKey(mmsiShip)) {
                                destination = AISMSG5.getDest();
                                Ship ship = shipsToSouth.get(mmsiShip);
                                ship.setDestin(destination);
                                shipsToSouth.put(mmsiShip, ship);
//                                writeFile("\n" + ship, pathToWriteFileToSouth);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Can not read!!!");
        }
    }

    private static void writeFile(String data, String pathToFile) {
        File file = new File(pathToFile);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true))){
            bufferedWriter.write(data);
        } catch (IOException e) {
            throw new RuntimeException("Can not write file " + file, e);
        }
    }
}
