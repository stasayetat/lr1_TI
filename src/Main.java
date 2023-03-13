import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String data = new String();
        try {
            File myFile = new File("C:\\Users\\stasy\\IdeaProjects\\lr1_TI\\src\\filename.txt");
            Scanner reader = new Scanner(myFile);
            while(reader.hasNextLine()) {
                data += reader.nextLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error");
        }
        int countLetters = 0;
        Map<String, Integer> res = new HashMap<>();
        for(int i = 0; i < data.length(); i++) {
            char dataChar = data.charAt(i);
           if((int)dataChar >= 65 && (int)dataChar <= 122) {
               if((int)dataChar <= 90) {
                   addChar(res, String.valueOf(dataChar));
                   countLetters++;
               } else if((int)dataChar >= 97) {
                   addChar(res, String.valueOf(dataChar));
                   countLetters++;
               }
           }
        }
        Map<String, Double> probRes = new HashMap<>();
        for(Map.Entry<String, Integer> el: res.entrySet()) {
            Double prob = Double.valueOf(el.getValue())/countLetters;
            probRes.put(el.getKey(), prob);
        }
        for(Map.Entry<String, Double> el: probRes.entrySet()) {
            System.out.print(el.getKey() + " - ");
            printDouble(el.getValue());
            System.out.println("\n");
        }
        System.out.println("Загалом символів: " + countLetters);
        System.out.println("Середня ймовірність: " + Double.valueOf(res.size())/countLetters);

        Map<String, Double> informRes = new HashMap<>();
        for(Map.Entry<String, Double> el : probRes.entrySet()) {
            Double infSymbol = Math.log(el.getValue()) / Math.log(2) * -1;
            informRes.put(el.getKey(), infSymbol);
        }
        System.out.println(informRes);
        printInformName();
        Map<String, Double> entropyRes = new HashMap<>();
        Double entropySum = 0d;
        for(Map.Entry<String, Double> el: probRes.entrySet()) {
            Double entropySymbol = el.getValue() * Math.log(probRes.get(el.getKey())) / Math.log(2) * -1;
            entropyRes.put(el.getKey(), entropySymbol);
            entropySum += entropySymbol;
        }
        System.out.println(entropyRes);
        System.out.println("Ентропія всього тексту: " + entropySum);
    }

    public static void addChar(Map map, String data) {
        if(map.containsKey(data)) {
            map.put(data, (Integer)map.get(data) + 1);
        } else {
            map.put(data, 1);
        }
    }

    public static void printDouble(Double number) {
        System.out.printf("%.9f", number);
    }

    public static void printInformName() {
        double allInform = 0;
        String name = "Stanislav Yarets";
        Map<String, Integer> nameMap = new HashMap<>();
        int allSymbols = 0;
        for(int i = 0; i < name.length(); i++) {
            char dataChar = name.charAt(i);
            if((int)dataChar >= 65 && (int)dataChar <= 122) {
                if((int)dataChar <= 90) {
                    addChar(nameMap, String.valueOf(dataChar));
                    allSymbols++;
                } else if((int)dataChar >= 97) {
                    addChar(nameMap, String.valueOf(dataChar));
                    allSymbols++;
                }
            }
        }

        Map<String, Double> probName = new HashMap<>();
        for(Map.Entry<String, Integer> el : nameMap.entrySet()) {
            probName.put(el.getKey(), Double.valueOf(el.getValue())/allSymbols);
        }

        for(Map.Entry<String, Double> el : probName.entrySet()) {
            allInform += (Math.log(el.getValue())/Math.log(2)*-1) * nameMap.get(el.getKey());
        }
        System.out.println("Інформація мого імені - " + allInform);

    }
}