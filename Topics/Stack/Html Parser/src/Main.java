import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

class Main {
    static int temp = -1;

    private static Deque<String[]> parseHtmlChild(String html) {
        temp++;
        Deque<String[]> htmlTags = new ArrayDeque<>();

        // find the open tag
        int startIndexOpen = 0;
        int endIndexOpen = 0;
        for (int i = 0; i < html.length(); i++) {
            if (html.toCharArray()[i] == '<') {
                startIndexOpen = i;
            } else if (html.toCharArray()[i] == '>') {
                endIndexOpen = i;
                break;
            }
        }

        // find the close tag
        int startIndexClose = endIndexOpen + 1;
        int endIndexClose;
        for (int i = endIndexOpen + 1; i < html.length(); i++) {
            if (html.toCharArray()[i] == '<' && html.toCharArray()[i + 1] == '/') {
                startIndexClose = i;
            } else if (html.toCharArray()[i] == '<') {
                Deque<String[]> childs = parseHtmlChild(html.substring(i));
                for (String[] temp: childs)
                    System.out.println(temp[1]);
                for (String[] tempChild : childs) {
                    i += Integer.parseInt(tempChild[2]);
                }
                i -= 1;
            } else if (html.toCharArray()[i] == '>') {
                endIndexClose = i;
                htmlTags.push(new String[] {html.substring(startIndexOpen, endIndexClose + 1), html.substring(endIndexOpen + 1, startIndexClose), String.valueOf(html.substring(startIndexOpen, endIndexClose + 1).length())});
                return htmlTags;
            }
        }

        return htmlTags;
    }

    public static void main(String[] args) {
        Deque<String[]> htmlTags;

        Scanner scanner = new Scanner(System.in);

        String fullHtml = scanner.nextLine();

        htmlTags = parseHtmlChild(fullHtml);

        for (String[] temp: htmlTags)
            System.out.println(temp[1]);
    }
}