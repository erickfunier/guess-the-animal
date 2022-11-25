package animals;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import javax.management.modelmbean.XMLParseException;
import java.io.*;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    static String[] morning_greetings = {"Good morning!"};
    static String[] afternoon_greetings = {"Good afternoon!"};
    static String[] evening_greetings = {"Good evening!"};
    static String[] bye_greetings = {"Bye!", "See you soon!", "Have a nice day!"};

    private static void printHelloGreetings() {
        Random random = new Random();

        if (LocalTime.now().compareTo(LocalTime.parse("12:00:00")) < 0) {
            // morning
            System.out.println(morning_greetings[random.nextInt(morning_greetings.length)] + "\n");
        } else if (LocalTime.now().compareTo(LocalTime.parse("18:00:00")) < 0) {
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

    private static String startGame(BinaryTree knowledgeTree, Scanner scanner) {
        if (knowledgeTree.root != null) {
            System.out.println("I know a lot about animals.");
            System.out.println("Let's play a game!\nYou think of an animal, and I guess it.\nPress enter when you're ready.");
            scanner.nextLine();
            return null;
        } else {
            System.out.println("I want to learn about animals.\nWhich animal do you like most?");
            String animal = identifyTheAnimal(scanner);

            System.out.println("Wonderful! I've learned so much about animals!\nLet's play a game!\nYou think of an animal, and I guess it.\nPress enter when you're ready.");
            scanner.nextLine();
            return animal;
        }
    }

    private static boolean checkIfAnimalNode(Node node) {
        return node.getData().startsWith("a");
    }

    public static void main(String[] args) throws IOException {
        List<String> aff_responses = List.of("y", "yes", "yeah", "yep", "sure", "right", "affirmative", "correct", "indeed", "you bet", "exactly", "you said it");
        List<String> neg_responses = List.of("n", "no", "no way", "nah", "nope", "negative", "i don't think so, yeah no", "i don't think so", "yeah no");
        List<String> confirmations = List.of(
                "I'm not sure I caught you: was it yes or no?",
                "Funny, I still don't understand, is it yes or no?",
                "Oh, it's too complicated for me: just tell me yes or no.",
                "Could you please simply say yes or no?",
                "Oh, no, don't try to confuse me: say yes or no.");
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        String type = null;
        if (args.length > 0)
            type = args[1];

        String fileName = "animals.json";
        if (type != null)
            switch (type) {
                case "xml" -> fileName = "animals.xml";
                case "yaml" -> fileName = "animals.yaml";
            }


        printHelloGreetings();
        BinaryTree knowledgeTree = loadKnowledgeTree(fileName, type);
        String animal = startGame(knowledgeTree, scanner);

        boolean playing = true;

        while (playing) {
            if (animal == null) {

                Node[] node = playGame(null, knowledgeTree.root, aff_responses, neg_responses, confirmations, random, scanner);
                if (node[0] != null) {
                    System.out.println("I give up. What animal do you have in mind?");
                    String secondAnimal = identifyTheAnimal(scanner);

                    animal = node[1].getData();
                    System.out.println("Specify a fact that distinguishes " + node[1].getData() + " from " + secondAnimal + ".\n" +
                            "The sentence should satisfy one of the following templates:\n- It can ...\n- It has ...\n- It is a/an ...'\n");

                    String fact = scanner.nextLine().replaceAll("\\p{P}$", "");

                    if (!fact.toLowerCase().startsWith("it can") && !fact.toLowerCase().startsWith("it is") && !fact.toLowerCase().startsWith("it has")) {
                        System.out.println("The examples of a statement:\n" +
                                "- It can fly\n"+
                                "- It has horn\n"+
                                "- It is a mammal\n"+
                                "Specify a fact that distinguishes " + node[1].getData() + " from " + secondAnimal + ".\n" +
                                "The sentence should be of the format: 'It can/has/is ...'.");
                        fact = scanner.nextLine().replaceAll("\\p{P}$", "");
                    }

                    System.out.println("Is the statement correct for " + secondAnimal + "?");

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

                    System.out.println("I have learned the following facts about animals:");
                    knowledgeTree.traversePreOrder(tempNode);

                    System.out.println("I can distinguish these animals by asking the question:");
                    System.out.println(getQuestion(tempNode.getData()) + "?\n");

                    System.out.println("I've learned so much about animals!\n");
                }
            } else {
                System.out.println("Is it " + animal + "?");

                String response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                while (!aff_responses.contains(response) && !neg_responses.contains(response)) {
                    System.out.println(confirmations.get(random.nextInt(confirmations.size())));

                    response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                }

                String secondAnimal = null;
                if (neg_responses.contains(response)) {
                    System.out.println("I give up. What animal do you have in mind?");

                    secondAnimal = identifyTheAnimal(scanner);

                    System.out.println("Specify a fact that distinguishes " + animal + " from " + secondAnimal + ".\n" +
                            "The sentence should satisfy one of the following templates:\n- It can ...\n- It has ...\n- It is a/an ...'\n");
                }

                String fact = scanner.nextLine().replaceAll("\\p{P}$", "");

                if (!fact.toLowerCase().startsWith("it can") && !fact.toLowerCase().startsWith("it is") && !fact.toLowerCase().startsWith("it has")) {
                    System.out.println("The examples of a statement:\n" +
                            "- It can fly\n"+
                            "- It has horn\n"+
                            "- It is a mammal\n"+
                            "Specify a fact that distinguishes " + animal + " from " + secondAnimal + ".\n" +
                            "The sentence should be of the format: 'It can/has/is ...'.");
                    fact = scanner.nextLine().replaceAll("\\p{P}$", "");
                }

                assert secondAnimal != null;
                System.out.println("Is the statement correct for " + secondAnimal + "?");

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

                System.out.println("I have learned the following facts about animals:");
                knowledgeTree.traversePreOrder(knowledgeTree.root);

                System.out.println("I can distinguish these animals by asking the question:");
                System.out.println(getQuestion(knowledgeTree.root.getData()) + "?\n");

                System.out.println("I've learned so much about animals!\n");
            }

            System.out.println("Would you like to play again?");

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

                objectMapper
                        .writerWithDefaultPrettyPrinter()
                        .writeValue(new File(fileName), knowledgeTree.root);
            } else {
                animal = null;
                System.out.println("Wonderful!\nLet's play a game!\nYou think of an animal, and I guess it.\nPress enter when you're ready.");
                scanner.nextLine();
            }
        }

        System.out.println("\n" + bye_greetings[random.nextInt(bye_greetings.length)]);
    }

    private static Node[] playGame(Node parentNode, Node node, List<String> aff_responses, List<String> neg_responses, List<String> confirmations, Random random, Scanner scanner) {
        if (node != null) {
            if (node.getData().toLowerCase().startsWith("it")) {
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
                System.out.println("Is it " + node.getData() + "?");
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
        String[] neg = {"can't", "doesn't have", "isn't"};
        String[] pos = {"it can", "it has", "it is"};
        String[] quest = {"Can it", "Does it have", "Is it"};


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
        String animalWithPrefix = null;

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


