import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        StringBuilder data = new StringBuilder();
        try {
            File myFile = new File("C:\\Users\\stasy\\IdeaProjects\\lr1_TI\\src\\filename.txt");
            Scanner reader = new Scanner(myFile);
            while(reader.hasNextLine()) {
                data.append(reader.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error");
        }
        Map<String, Integer> res = new HashMap<>();
        int countLetters = countFile(data.toString(), res);
        Map<String, Double> probRes = new HashMap<>();
        countProbMap(countLetters, res, probRes);
        System.out.println(res);
        System.out.println(probRes.values());
        for(Map.Entry<String, Double> el: probRes.entrySet()) {
            System.out.print(el.getKey() + " - ");
            printDouble(el.getValue());
        }
        System.out.println("Загалом символів: " + countLetters);
        System.out.println("Середня ймовірність: " + Double.valueOf(res.size())/countLetters);

        Map<String, Double> informRes = new HashMap<>();
        countInform(informRes, probRes);
        System.out.println("Інформація тексту: " + informRes);
        Map<String, Double> entropyRes = new HashMap<>();
        double entropySum = countEntropy(entropyRes, probRes);
        System.out.println("Ентропія кожної літери: " + entropyRes);
        System.out.println("Ентропія всього тексту H(X): " + entropySum);
        System.out.println("\n\n\n");
        printInformName();
        System.out.println("\n\n\n");
        String taskAlphabet = "abcd";
        Map<String, Integer> numberTask = new HashMap<>();
        int countTask = countFile(taskAlphabet, numberTask);
        Map<String, Double> probTask = new HashMap<>();
        countProbMap(countTask, numberTask, probTask);
        Map<String, Double> entropyTask =  new HashMap<>();
        double entropyAllNumber = countEntropy(entropyTask, probTask);
        System.out.println("Ентропія Х: " + entropyAllNumber);
        double[][] taskLr = {{0.5, 0.5, 0, 0}, {0, 0.5, 0.5, 0}, {0, 0, 0.5, 0.5}, {0.5, 0, 0, 0.5}};
        double partEntropy = findPartEntropy(taskLr, 2);
        System.out.println("Часткова умовна ентропія H(Y/x2): " + partEntropy);
        double[][] compatTask = createCompatMatrix(taskLr, probTask);
        double totalEntropy = findTotalEntropy(taskLr, compatTask);
        System.out.println("Повна умовна ентропія: " + totalEntropy);
        double compatEntropyTask = findCompatEntropy(compatTask);
        System.out.println("Сумісна ентропія: " + compatEntropyTask);
        double mutEntropyFirst = findMutEntropy(compatTask, probTask);
        System.out.println("Взаємна ентропія за 1 формулою: " + mutEntropyFirst);
        System.out.println("Взаємна ентропія за 2 формулою: " + (entropyAllNumber - totalEntropy));
        System.out.println("Взаємна ентропія за 3 формулою: " + (entropyAllNumber * 2 - compatEntropyTask));
    }

    public static void addChar(Map map, String data) {
        if(map.containsKey(data)) {
            map.put(data, (Integer)map.get(data) + 1);
        } else {
            map.put(data, 1);
        }
    }

    public static void printDouble(Double number) {
        System.out.printf("%.9f\n", number);
    }

    public static void printInformName() {
        double allInform = 0;
        String name = "StanislavYarets";
        Map<String, Integer> nameMap = new HashMap<>();
        int allSymbols = countFile(name, nameMap);
        Map<String, Double> probName = new HashMap<>();
        countProbMap(allSymbols, nameMap, probName);
        Map<String, Double> informMap = new HashMap<>();
        countInform(informMap, probName);
        for(Map.Entry<String, Integer> nameEntry : nameMap.entrySet()) {
            allInform += (informMap.get(nameEntry.getKey()) * nameEntry.getValue());
        }
        System.out.println("Інформація мого імені - \n" + allInform);
        System.out.println("Вся інформація - \n" + informMap);

    }

    public static int countFile(String data, Map<String, Integer> res) {
        int countLetters = 0;
        for(int i = 0; i < data.length(); i++) {
            char dataChar = data.charAt(i);
            if((int)dataChar >= 65 && (int)dataChar <= 122) {
                if((int)dataChar <= 90) {
                    addChar(res, String.valueOf(dataChar));
                } else if((int)dataChar >= 97) {
                    addChar(res, String.valueOf(dataChar));
                }
                countLetters++;
            }
        }
        return countLetters;
    }

    public static void countProbMap(int allSymbols, Map<String, Integer> countMap, Map<String, Double> probMap) {
        for(Map.Entry<String, Integer> el : countMap.entrySet()) {
            probMap.put(el.getKey(), Double.valueOf(el.getValue())/allSymbols);
        }
    }

    public static void countInform(Map<String, Double> informMap, Map<String, Double> probMap) {
        for(Map.Entry<String, Double> el : probMap.entrySet()) {
            Double infSymbol = Math.log(el.getValue()) / Math.log(2) * -1;
            informMap.put(el.getKey(), infSymbol);
        }
    }

    public static double countEntropy(Map<String, Double> entropyMap, Map<String, Double> probMap) {
        double entropySum = 0;
        for(Map.Entry<String, Double> el: probMap.entrySet()) {
            double entropySymbol = el.getValue() * Math.log(el.getValue()) / Math.log(2) * -1;
            entropyMap.put(el.getKey(), entropySymbol);
            entropySum += entropySymbol;
        }
        return entropySum;
    }

    public static double findPartEntropy(double[][] task, int x) {
        double res = 0;
        for(int i = 0; i < task[x-1].length; i++) {
            if(task[x-1][i] == 0)
                continue;
            res += (task[x-1][i] * (Math.log(task[x-1][i]) / Math.log(2) * -1));
        }
        return res;
    }

    public static double[][] createCompatMatrix(double[][] task, Map<String, Double> map) {
        double[][] res = new double[4][4];
        Collection<Double> mapProb = map.values();
        double[] mapEntry = mapProb.stream().mapToDouble(d -> d).toArray();
        for(int i = 0; i < task.length; i++) {
            for(int j = 0; j < task[i].length; j++) {
                res[i][j] = task[i][j] * mapEntry[i];
            }
        }
        return res;
    }

    public static double findTotalEntropy(double[][] task, double[][] partTask) {
        double res = 0;
        for(int i = 0; i < task.length; i++) {
            for(int j = 0; j < task[i].length; j++) {
                if(task[i][j] == 0)
                    continue;
                res += partTask[i][j] * (Math.log(task[i][j])/Math.log(2))* -1;
            }
        }
        return res;
    }

    public static double findCompatEntropy(double[][] partTask) {
        double res = 0;
        for(int i = 0; i < partTask.length; i++) {
            for(int j = 0; j < partTask[i].length; j++) {
                if(partTask[i][j] == 0)
                    continue;
                res += partTask[i][j] * (Math.log(partTask[i][j])/Math.log(2))* -1;
            }
        }
        return res;
    }

    public static double findMutEntropy(double[][] task, Map<String, Double> map) {
        double res = 0;
        Collection<Double> mapProb = map.values();
        double[] mapEntry = mapProb.stream().mapToDouble(d -> d).toArray();
        for(int i = 0; i < task.length; i++) {
            for(int j = 0; j < task[i].length; j++) {
                if(task[i][j] == 0)
                    continue;
                res += task[i][j] * (Math.log((task[i][j])/(mapEntry[i])) / Math.log(2) * -1);
            }
        }
        return res;
    }
}