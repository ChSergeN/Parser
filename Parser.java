import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class Parser {

    public static void main(String[] args) throws IOException {
        ArrayList<String> listOfSeparators = new ArrayList<String>();
        listOfSeparators.add(" ");
        listOfSeparators.add(",");
        listOfSeparators.add(".");
        listOfSeparators.add("!");
        listOfSeparators.add("?");
        listOfSeparators.add("(");
        listOfSeparators.add(")");
        listOfSeparators.add("[");
        listOfSeparators.add("]");
        listOfSeparators.add("-");
        listOfSeparators.add(";");
        listOfSeparators.add(":");
        listOfSeparators.add("\n");
        listOfSeparators.add("\r");
        listOfSeparators.add("\t");
        //System.out.println(getPage());


        String pagetext = getPage().text();
        //System.out.println(pagetext);
        String separatorsString = String.join("|\\", listOfSeparators);
        //System.out.println(separatorsString);
        Map<String, Word> countMap = new HashMap<String, Word>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(pagetext.getBytes(StandardCharsets.UTF_8))));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] words = line.split(separatorsString);
            for (String word : words) {
                if ("".equals(word)) {
                    continue;
                }

                Word wordObj = countMap.get(word);
                if (wordObj == null) {
                    wordObj = new Word();
                    wordObj.word = word;
                    wordObj.count = 0;
                    countMap.put(word, wordObj);
                }

                wordObj.count++;
            }
        }


        reader.close();

        SortedSet<Word> sortedWords = new TreeSet<Word>(countMap.values());

        for (Word word : sortedWords) {
            System.out.println(word.count + "\t" + word.word);
        }
    }

    private static String readWebAddress() {
        String wAddress;
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("Введите адрес Web-страницы: ");
            String webAddress = in.nextLine();
            in.close();
            if (checkUrl(webAddress)) {
                wAddress = webAddress;
                break;
            }

        }
        in.close();
        return wAddress;
    }

    public static boolean checkUrl(String s) {
            return s != null && s.matches("^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
    }

    static boolean isValidURL(String url) {
        try {
            new URI(url).parseServerAuthority();
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private static Document getPage() throws IOException {
        String url = readWebAddress();
        // https://www.simbirsoft.com/
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    public static class Word implements Comparable<Word> {
        String word;
        int count;

        @Override
        public int hashCode() { return word.hashCode(); }

        @Override
        public boolean equals(Object obj) { return word.equals(((Word)obj).word); }

        @Override
        public int compareTo(Word b) { return b.count - count; }
    }
}