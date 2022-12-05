package animals;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.*;
import java.time.LocalTime;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static animals.BinaryTree.getFactWithPrefix;

public class Main {
    static String fileName = "animals.json";
    static String type = null;

    static List<String> listOfAnimals;
    static List<String[]> listOfFacts;

    static BinaryTree knowledgeTree;

    static long numberOfNodes = 0;
    static long numberOfAnimals = 0;
    static long minAnimalDepth = 0;
    static long maxAnimalDepth = 0;

    static ResourceBundle appResource = ResourceBundle.getBundle("animals.App");

    private static void printHelloGreetings() {
        String[] morning_greetings = (String[]) appResource.getObject("morning_greetings");
        String[] afternoon_greetings = (String[]) appResource.getObject("afternoon_greetings");
        String[] evening_greetings = (String[]) appResource.getObject("evening_greetings");
        Random random = new Random();

        if (LocalTime.now().isBefore(LocalTime.parse("12:00:00"))) {
            // morning
            System.out.println(morning_greetings[random.nextInt(morning_greetings.length)] + "\n");
        } else if (LocalTime.now().isBefore(LocalTime.parse("18:00:00"))) {
            // afternoon
            System.out.println(afternoon_greetings[random.nextInt(afternoon_greetings.length)] + "\n");
        } else {
            // evening
            System.out.println(evening_greetings[random.nextInt(evening_greetings.length)] + "\n");
        }
    }

    private static BinaryTree loadKnowledgeTree(String filename, String type) {
        if (type != null) {
            switch (type) {
                case "xml":
                    ObjectMapper objectMapper = new XmlMapper();
                    try (Reader reader = new FileReader(filename)) {
                        Node root = objectMapper.readValue(reader, Node.class);
                        return new BinaryTree(root);
                    } catch (IOException e) {
                        return new BinaryTree(null);
                    }
                case "yaml":
                    objectMapper = new YAMLMapper();
                    try (Reader reader = new FileReader(filename)) {
                        Node root = objectMapper.readValue(reader, Node.class);
                        return new BinaryTree(root);
                    } catch (IOException e) {
                        return new BinaryTree(null);
                    }
                default:
                    objectMapper = new JsonMapper();
                    try (Reader reader = new FileReader(filename)) {
                        Node root = objectMapper.readValue(reader, Node.class);
                        return new BinaryTree(root);
                    } catch (JsonMappingException | JsonParseException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        return new BinaryTree(null);
                    }
            }
        } else {
            ObjectMapper objectMapper = new JsonMapper();
            try (Reader reader = new FileReader(filename)) {
                Node root = objectMapper.readValue(reader, Node.class);
                return new BinaryTree(root);
            } catch (JsonMappingException | JsonParseException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                return new BinaryTree(null);
            }
        }
    }

    private static void startGame(Scanner scanner) {
        if (knowledgeTree.root != null) {
            startMenu(scanner, null);
        } else {
            System.out.println(appResource.getObject("start_game"));
            String animal = identifyTheAnimal(scanner);

            startMenu(scanner, animal);
        }
    }

    private static void searchAnimal(String animal) {
        listOfFacts = new ArrayList<>();
        findAnimalPrint(null, knowledgeTree.root, animal);

        if (listOfFacts.size() > 0) {
            var knownFacts = (UnaryOperator) appResource.getObject("known_facts");

            System.out.println(knownFacts.apply(getPrefix() + removePrefix(animal)));
            for (int i = listOfFacts.size() - 1; i >= 0; i--) {
                if (Locale.getDefault().getLanguage().equals("eo")) {
                    System.out.println("- Ĝi " + getFactWithPrefix(listOfFacts.get(i)[0], listOfFacts.get(i)[1]) + ".");
                } else {
                    System.out.println("- It " + getFactWithPrefix(listOfFacts.get(i)[0], listOfFacts.get(i)[1]) + ".");
                }
            }
        } else {
            var knownFacts = (UnaryOperator) appResource.getObject("known_no_facts");

            System.out.println(knownFacts.apply(getPrefix() + removePrefix(animal)));
        }

    }

    public static int heightOfTree(Node root) {
        if (null == root)
            return 0;
        int hLeftSub = heightOfTree(root.getNo());
        int hRightSub = heightOfTree(root.getYes());
        return Math.max(hLeftSub, hRightSub) + 1;
    }

    private static void getStats(Node node) {
        if (node == null)
            return;

        getStats(node.getNo());

        numberOfNodes++;
        if (node.isLeaf()) {
            if (minAnimalDepth == 0) {
                minAnimalDepth++;
            }
            maxAnimalDepth++;
            numberOfAnimals++;
        }

        getStats(node.getYes());
    }

    private static void calcStats() {
        System.out.println(appResource.getString("knowledge_stats"));

        getStats(knowledgeTree.root);

        double avgDepth = (double)numberOfNodes/(double)maxAnimalDepth;
        System.out.println(appResource.getString("stats_root_node") + knowledgeTree.root.getData());
        System.out.println(appResource.getString("stats_num_nodes") + numberOfNodes);
        System.out.println(appResource.getString("stats_num_animals") + numberOfAnimals);
        System.out.println(appResource.getString("stats_num_statements") + (numberOfNodes - numberOfAnimals));
        System.out.println(appResource.getString("stats_height") + (heightOfTree(knowledgeTree.root) - 1));
        System.out.println(appResource.getString("stats_min") + minAnimalDepth);
        System.out.println(appResource.getString("stats_max") + maxAnimalDepth);
        System.out.format(appResource.getString("stats_avg") + "%.1f\n", avgDepth);
    }

    private static void startMenu(Scanner scanner, String animal) {
        String[] bye_greetings = (String[]) appResource.getObject("bye_greetings");
        Random random = new Random();
        System.out.println(appResource.getString("start_menu"));
        System.out.println(appResource.getString("print_menu"));

        int option = scanner.nextInt();
        scanner.nextLine();

        while(option != 0) {
            switch (option) {
                case 1:
                    playGame(scanner, animal);
                    break;
                case 2:
                    listAllAnimals();
                    break;
                case 3:
                    System.out.println(appResource.getString("insert_animal"));
                    String animalToSearch = scanner.nextLine();
                    searchAnimal(animalToSearch);
                    break;
                case 4:
                    calcStats();
                    break;
            }

            System.out.println(appResource.getString("print_menu"));
            option = scanner.nextInt();
            scanner.nextLine();
            if (option == 0) {
                System.out.println("\n" + bye_greetings[random.nextInt(bye_greetings.length)]);
            }
        }
    }

    public static Node findAnimalPrint(Node parent, Node node, String animal) {
        if (node == null)
            return null;

        //System.out.println(node.getData());
        if (node.isLeaf() && node.getData().contains(animal)) {
            if (Objects.equals(parent.getYes().getData(), node.getData())) {
                listOfFacts.add(new String[] {parent.getData(), "pos"});
            } else {
                listOfFacts.add(new String[] {parent.getData(), "neg"});
            }
            return node;
        }

        Node returned = findAnimalPrint(node, node.getNo(), animal);

        if (returned == null) {
            returned = findAnimalPrint(node, node.getYes(), animal);
        }

        if (parent != null && returned != null) {
            if (Objects.equals(parent.getYes().getData(), node.getData())) {
                listOfFacts.add(new String[] {parent.getData(), "pos"});
            } else {
                listOfFacts.add(new String[] {parent.getData(), "neg"});
            }
        }

        return returned;
    }

    public static void inOrderPrint(Node node) {
        if (node == null)
            return;
        inOrderPrint(node.getNo());
        if (node.isLeaf()) {
            listOfAnimals.add(removePrefix(node.getData()));
        }

        inOrderPrint(node.getYes());
    }

    public static void listAllAnimals() {
        System.out.println("Here are the animals I know:");
        listOfAnimals = new ArrayList<>();
        inOrderPrint(knowledgeTree.root);

        listOfAnimals.sort(Comparator.naturalOrder());
        for (String animal : listOfAnimals) {

            System.out.println("- " + animal);
        }
    }

    public static String getPrefix() {
        Locale locale = Locale.getDefault();

        if (locale.getLanguage().equals("eo")) {
            return "la ";
        } else {
            return "the ";
        }
    }

    private static String removePrefix(String animal) {
        return animal.replace("a ", "").replace("an ", "");
    }

    public static void playGame(Scanner scanner, String animal) {
        System.out.println(appResource.getString("play_game"));
        scanner.nextLine();
        Random random = new Random();
        List<String> aff_responses = List.of("jes", "y", "yes", "yeah", "yep", "sure", "right", "affirmative", "correct", "indeed", "you bet", "exactly", "you said it");
        List<String> neg_responses = List.of("ne", "n", "no", "no way", "nah", "nope", "negative", "i don't think so, yeah no", "i don't think so", "yeah no");
        List<String> confirmations = List.of(
                "I'm not sure I caught you: was it yes or no?",
                "Funny, I still don't understand, is it yes or no?",
                "Oh, it's too complicated for me: just tell me yes or no.",
                "Could you please simply say yes or no?",
                "Oh, no, don't try to confuse me: say yes or no.");

        boolean playing = true;

        while (playing) {
            if (animal == null) {

                Node[] node = playGame(null, knowledgeTree.root, aff_responses, neg_responses, confirmations, random, scanner);
                if (node[0] != null) {
                    System.out.println(appResource.getString("give_up"));
                    String secondAnimal = identifyTheAnimal(scanner);

                    animal = node[1].getData();
                    var specifyFact = (UnaryOperator) appResource.getObject("specify_fact");

                    System.out.println(((String[]) specifyFact.apply(new String[]{getAnimalWithAndWithoutPrefix(node[1].getData())[1], getAnimalWithAndWithoutPrefix(secondAnimal)[1]}))[0]);

                    String fact = scanner.nextLine().replaceAll("\\p{P}$", "");

                    if ((!fact.toLowerCase().startsWith("it can") &&
                            !fact.toLowerCase().startsWith("it is") &&
                            !fact.toLowerCase().startsWith("it has")) &&
                        (!fact.toLowerCase().startsWith("ĝi povas") &&
                                !fact.toLowerCase().startsWith("ĝi estas") &&
                                !fact.toLowerCase().startsWith("ĝi havas") &&
                                !fact.toLowerCase().startsWith("ĝi loĝas"))) {
                        System.out.println(appResource.getString("fact_example"));
                        System.out.println(((String[]) specifyFact.apply(new String[]{getAnimalWithAndWithoutPrefix(node[1].getData())[1], getAnimalWithAndWithoutPrefix(secondAnimal)[1]}))[0]);
                        fact = scanner.nextLine().replaceAll("\\p{P}$", "");
                    }

                    var factConfirmation = (UnaryOperator) appResource.getObject("fact_confirmation");
                    var animalName = (UnaryOperator) appResource.getObject("animal.name");
                    System.out.println(factConfirmation.apply(animalName.apply(getAnimalWithAndWithoutPrefix(secondAnimal)[1])));

                    String response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                    String factWithoutPrefix = Arrays.stream(fact.split(" ")).skip(2).collect(Collectors.joining(" "));
                    while (!aff_responses.contains(response) && !neg_responses.contains(response)) {
                        System.out.println(confirmations.get(random.nextInt(confirmations.size())));

                        response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                    }
                    Node tempNode = knowledgeTree.addPreOrder(knowledgeTree.root, node[1], fact, "");

                    if (aff_responses.contains(response)) {
                        knowledgeTree.addPreOrder(knowledgeTree.root, tempNode, animal, "NO");
                        knowledgeTree.addPreOrder(knowledgeTree.root, tempNode, secondAnimal, "YES");
                    } else {
                        knowledgeTree.addPreOrder(knowledgeTree.root, tempNode, animal, "YES");
                        knowledgeTree.addPreOrder(knowledgeTree.root, tempNode, secondAnimal, "NO");
                    }


                    System.out.println(appResource.getString("learned"));
                    knowledgeTree.traversePreOrder(tempNode);

                    System.out.println(appResource.getString("distinguish"));
                    System.out.println(getQuestion(tempNode.getData()) + "?\n");

                    System.out.println(appResource.getString("learned_so_much"));
                }
            } else {
                var animalQuestion = (UnaryOperator) appResource.getObject("animal.question");
                var animalName = (UnaryOperator) appResource.getObject("animal.name");

                System.out.println(animalQuestion.apply(animalName.apply(getAnimalWithAndWithoutPrefix(animal)[1])));

                String response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                while (!aff_responses.contains(response) && !neg_responses.contains(response)) {
                    System.out.println(confirmations.get(random.nextInt(confirmations.size())));

                    response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                }

                String secondAnimal = null;
                var specifyFact = (UnaryOperator) appResource.getObject("specify_fact");
                if (neg_responses.contains(response)) {
                    System.out.println(appResource.getString("give_up"));

                    secondAnimal = identifyTheAnimal(scanner);


                    System.out.println(((String[]) specifyFact.apply(new String[]{getAnimalWithAndWithoutPrefix(animal)[1], getAnimalWithAndWithoutPrefix(secondAnimal)[1]}))[0]);
                }

                String fact = scanner.nextLine().replaceAll("\\p{P}$", "");

                if ((!fact.toLowerCase().startsWith("it can") &&
                        !fact.toLowerCase().startsWith("it is") &&
                        !fact.toLowerCase().startsWith("it has")) &&
                        (!fact.toLowerCase().startsWith("ĝi povas") &&
                                !fact.toLowerCase().startsWith("ĝi estas") &&
                                !fact.toLowerCase().startsWith("ĝi havas") &&
                                !fact.toLowerCase().startsWith("ĝi loĝas"))) {
                    System.out.println(appResource.getString("fact_example"));
                    System.out.println(((String[]) specifyFact.apply(new String[]{getAnimalWithAndWithoutPrefix(animal)[1], getAnimalWithAndWithoutPrefix(secondAnimal)[1]}))[0]);
                    fact = scanner.nextLine().replaceAll("\\p{P}$", "");
                }

                assert secondAnimal != null;
                var factConfirmation = (UnaryOperator) appResource.getObject("fact_confirmation");
                System.out.println(factConfirmation.apply(animalName.apply(getAnimalWithAndWithoutPrefix(secondAnimal)[1])));
                //System.out.println("Is the statement correct for " + secondAnimal + "?");

                response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                while (!aff_responses.contains(response) && !neg_responses.contains(response)) {
                    System.out.println(confirmations.get(random.nextInt(confirmations.size())));

                    response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                }
                knowledgeTree.add(fact, "");

                if (aff_responses.contains(response)) {
                    knowledgeTree.add(animal, "NO");
                    knowledgeTree.add(secondAnimal, "YES");
                } else {
                    knowledgeTree.add(animal, "YES");
                    knowledgeTree.add(secondAnimal, "NO");
                }

                System.out.println(appResource.getString("learned"));
                knowledgeTree.traversePreOrder(knowledgeTree.root);

                System.out.println(appResource.getString("distinguish"));
                System.out.println(getQuestion(knowledgeTree.root.getData()) + "?\n");

                System.out.println(appResource.getString("learned_so_much"));
            }

            System.out.println(appResource.getString("play_again"));

            String response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
            while (!aff_responses.contains(response) && !neg_responses.contains(response)) {
                System.out.println(confirmations.get(random.nextInt(confirmations.size())));

                response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
            }

            if (neg_responses.contains(response)) {
                playing = false;
                ObjectMapper objectMapper;
                if (type != null) {
                    switch (type) {
                        case "xml" -> objectMapper = new XmlMapper();
                        case "yaml" -> objectMapper = new YAMLMapper();
                        default -> objectMapper = new JsonMapper();
                    }
                } else {
                    objectMapper = new JsonMapper();
                }

                try {
                    objectMapper
                            .writerWithDefaultPrettyPrinter()
                            .writeValue(new File(fileName), knowledgeTree.root);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                animal = null;
                System.out.println(appResource.getString("start_new_game"));
                scanner.nextLine();
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (args.length > 0) {
            type = args[1];
        }

        if (type != null)
            switch (type) {
                case "xml" -> fileName = "animals.xml";
                case "yaml" -> fileName = "animals.yaml";
            }

        Locale locale = Locale.getDefault();

        if (locale.getLanguage().equals("eo")) {
            appResource = ResourceBundle.getBundle("animals.App_eo");
            String[] splitted = fileName.split("\\.");
            fileName = splitted[0].replace("animals", "animals_eo") + "." + splitted[1];
        }

        printHelloGreetings();
        knowledgeTree = loadKnowledgeTree(fileName, type);
        startGame(scanner);

    }

    private static Node[] playGame(Node parentNode, Node node, List<String> aff_responses, List<String> neg_responses, List<String> confirmations, Random random, Scanner scanner) {
        if (node != null) {
            if (node.getData().toLowerCase().startsWith("it") || node.getData().toLowerCase().startsWith("ĝi")) {
                System.out.println(getQuestion(node.getData()) + "?");
                String response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                while (!aff_responses.contains(response) && !neg_responses.contains(response)) {
                    System.out.println(confirmations.get(random.nextInt(confirmations.size())));

                    response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                }

                if (neg_responses.contains(response)) {
                    return playGame(node, node.getNo(), aff_responses, neg_responses, confirmations, random, scanner);
                } else {
                    return playGame(node, node.getYes(), aff_responses, neg_responses, confirmations, random, scanner);
                }


            } else {
                var animalQuestion = (UnaryOperator) appResource.getObject("animal.question");
                var animalName = (UnaryOperator) appResource.getObject("animal.name");

                System.out.println(animalQuestion.apply(animalName.apply(getAnimalWithAndWithoutPrefix(node.getData())[1])));
                String response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                while (!aff_responses.contains(response) && !neg_responses.contains(response)) {
                    System.out.println(confirmations.get(random.nextInt(confirmations.size())));

                    response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                }

                if (aff_responses.contains(response)) {
                    return new Node[] {null, node};
                }
            }
        }

        return new Node[] {parentNode, node};
    }



    public static String getQuestion(String fact) {
        String[] neg = {"can't", "doesn't have", "isn't", "ne povas", "ne havas", "ne estas", "me loĝas"};
        String[] pos = {"it can", "it has", "it is", "ĝi povas", "ĝi havas", "ĝi estas", "ĝi loĝas"};
        String[] quest = {"Can it", "Does it have", "Is it", "Ĉu ĝi povas", "Ĉu ĝi havas", "Ĉu ĝi estas", "Ĉu ĝi loĝas"};


        for (int i = 0; i < pos.length; i++) {
            if (fact.toLowerCase().contains(pos[i])) {
                return quest[i] + fact.toLowerCase().replace(pos[i], "");
            }
        }

        for (int i = 0; i < neg.length; i++) {
            if (fact.toLowerCase().contains(neg[i])) {
                return quest[i] + fact.toLowerCase().replace(neg[i], "");
            }
        }
        return "";
    }

    private static String identifyTheAnimal(Scanner scanner) {
        String animal = scanner.nextLine().toLowerCase();

        return getAnimalWithAndWithoutPrefix(animal)[0];
    }

    private static String[] getAnimalWithAndWithoutPrefix(String animal) {
        String vowels = "eaiouEAIOU";
        List<String> prefix = List.of("a", "an");
        String animalWithoutPrefix = null;
        String animalWithPrefix;

        if (animal.contains(" ")) {
            if (animal.split(" ")[0].equals("the")) {
                animal = Arrays.stream(animal.split(" ")).skip(1).collect(Collectors.joining(" "));
                animalWithoutPrefix = animal;

                if (vowels.contains(animal.substring(0, 1))) {
                    animalWithPrefix = "an " + animalWithoutPrefix;
                } else {
                    animalWithPrefix = "a " + animalWithoutPrefix;
                }

            } else if (prefix.contains(animal.split(" ")[0])) {
                animalWithoutPrefix = Arrays.stream(animal.split(" ")).skip(1).collect(Collectors.joining(" "));
                animalWithPrefix = animal;
            } else {
                if (vowels.contains(animal.substring(0, 1))) {
                    animalWithPrefix = "an " + animal;
                } else {
                    animalWithPrefix = "a " + animal;
                }
            }
        } else {
            animalWithoutPrefix = animal;

            if (vowels.contains(animal.substring(0, 1))) {
                animalWithPrefix = "an " + animalWithoutPrefix;
            } else {
                animalWithPrefix = "a " + animalWithoutPrefix;
            }
        }
        return new String[]{animalWithPrefix, animalWithoutPrefix};
    }
}


