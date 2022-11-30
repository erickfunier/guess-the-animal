import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class Main {

    private static String removeHtmlTag(String html) {
        return html.replace("<" + "html" + ">", "").replace("</" + "html" + ">", "");
    }

    private static Deque<String> parseHtmlChild(String html) {
        Deque<String> htmlTags = new ArrayDeque<>();

        while (html.length() > 1) {
            int startIndexTag = 0;
            int endIndexTag = 0;
            for (int i = 0; i < html.length(); i++) {
                if (html.toCharArray()[i] == '<') {
                    startIndexTag = i;
                    break;
                }
            }

            int skipCloseTag = 1;
            int skipOpenTag = 1;

            for (int i = startIndexTag + 1; i < html.length(); i++) {
                if (html.toCharArray()[i] == '>') {
                    if (skipCloseTag == 0 && skipOpenTag == 0) {
                        endIndexTag = i;
                        break;
                    } else {
                        if (skipCloseTag > 0)
                            skipCloseTag--;
                    }
                } else if (html.toCharArray()[i] == '<' && html.toCharArray()[i+1] == '/') {
                    skipOpenTag--;
                } else if (html.toCharArray()[i] == '<') {
                    skipOpenTag++;
                    skipCloseTag++;
                }
            }

            htmlTags.push(html.substring(startIndexTag, endIndexTag + 1));
            html = html.substring(endIndexTag + 1);
        }

        return htmlTags;
    }

    private static boolean hasChild(String elem){
        return elem.split("(?<=>)\\w+").length > 2;
    }

    private static void process(String tempHtml) {
        final Pattern PARAM_NAME = Pattern.compile("(?<=>)\\w+", Pattern.DOTALL);

        if (tempHtml.endsWith("<"))
            tempHtml = tempHtml.substring(0, tempHtml.length()-1);

        if (!hasChild(tempHtml)) {
            Matcher m = PARAM_NAME.matcher(tempHtml);
            if (m.find())
                System.out.println(m.group(0));
        } else {
            Pattern betweenTags = Pattern.compile("(?<=>)([^$]+?)(?<=<)", Pattern.DOTALL);
            Matcher m = betweenTags.matcher(tempHtml);
            if (m.find()) {
                m.results().forEach(temp -> process(String.valueOf(temp)));
            }

        }
    }

    public static void main(String[] args) {
        Deque<String> htmlTags;
        Deque<String> htmlContents = new ArrayDeque<>();

        Scanner scanner = new Scanner(System.in);

        String fullHtml = scanner.nextLine();

        fullHtml = removeHtmlTag(fullHtml);

        htmlTags = parseHtmlChild(fullHtml);

        for(String html : htmlTags) {
            process(html);
        }

        System.out.println(htmlTags);

    }
}