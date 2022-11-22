package animals;

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
        System.out.println("I want to learn about animals.\nWhich animal do you like most?");
    }


    public static void main(String[] args) {
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

        printHelloGreetings();

        Animal animal = new Animal(identifyTheAnimal(scanner));
        BinaryTree firstTree = new BinaryTree();
        firstTree.add(animal.getNameWithPrefix(), "");

        boolean playing = true;
        BinaryTree secondTree = new BinaryTree();
        while (playing) {
            System.out.println("Wonderful! I've learned so much about animals!\nLet's play a game!\nYou think of an animal, and I guess it.\nPress enter when you're ready.");
            scanner.nextLine();

            if (secondTree.root != null) {
                Node node = playGame(null, secondTree.root, aff_responses, neg_responses, confirmations, random, scanner);

                if (!(node.value instanceof String[])) {
                    Animal secondAnimal = new Animal(identifyTheAnimal(scanner));

                    System.out.println("I give up. What animal do you have in mind?");

                    animal = ((Animal)node.value);
                    System.out.println("Specify a fact that distinguishes " + ((Animal)node.value).getNameWithPrefix() + " from " + secondAnimal.getNameWithPrefix() + ".\n" +
                            "The sentence should satisfy one of the following templates:\n- It can ...\n- It has ...\n- It is a/an ...'\n");

                    String fact = scanner.nextLine();

                    if (!fact.toLowerCase().startsWith("it can") && !fact.toLowerCase().startsWith("it is") && !fact.toLowerCase().startsWith("it has")) {
                        System.out.println("The examples of a statement:\n" +
                                "- It can fly\n"+
                                "- It has horn\n"+
                                "- It is a mammal\n"+
                                "Specify a fact that distinguishes " + ((Animal)node.value).getNameWithPrefix() + " from " + secondAnimal.getNameWithPrefix() + ".\n" +
                                "The sentence should be of the format: 'It can/has/is ...'.");
                        fact = scanner.nextLine().replaceAll("\\p{P}$", "");
                    }

                    System.out.println("Is the statement correct for " + secondAnimal.getNameWithPrefix() + "?");

                    String response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                    String factWithoutPrefix = Arrays.stream(fact.split(" ")).skip(2).collect(Collectors.joining(" "));
                    while (!aff_responses.contains(response) && !neg_responses.contains(response)) {
                        System.out.println(confirmations.get(random.nextInt(confirmations.size())));

                        response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                    }
                    node = secondTree.addPreOrder(secondTree.root, node, new String[]{factWithoutPrefix, fact}, "");

                    if (aff_responses.contains(response)) {
                        animal.addFact(new String[]{factWithoutPrefix, fact.split(" ")[1], "no"});
                        secondAnimal.addFact(new String[]{factWithoutPrefix, fact.split(" ")[1], "yes"});
                        secondTree.addPreOrder(secondTree.root, node, animal, "NO");
                        secondTree.addPreOrder(secondTree.root, node, secondAnimal, "YES");
                    } else {
                        animal.addFact(new String[]{factWithoutPrefix, fact.split(" ")[1], "yes"});
                        secondAnimal.addFact(new String[]{factWithoutPrefix, fact.split(" ")[1], "no"});
                        secondTree.addPreOrder(secondTree.root, node, animal, "YES");
                        secondTree.addPreOrder(secondTree.root, node, secondAnimal, "NO");
                    }

                    System.out.println("I have learned the following facts about animals:");
                    secondTree.traversePreOrder(node);

                    System.out.println("I can distinguish these animals by asking the question:");
                    System.out.println(getQuestion((String[])node.value) + "?\n");

                    System.out.println("Nice! I've learned so much about animals!\n");
                }
            } else {

                System.out.println("Is it " + animal.getNameWithPrefix() + "?");

                String response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                while (!aff_responses.contains(response) && !neg_responses.contains(response)) {
                    System.out.println(confirmations.get(random.nextInt(confirmations.size())));

                    response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                }

                Animal secondAnimal = null;
                if (neg_responses.contains(response)) {
                    System.out.println("I give up. What animal do you have in mind?");

                    secondAnimal = new Animal(identifyTheAnimal(scanner));

                    System.out.println("Specify a fact that distinguishes " + animal.getNameWithPrefix() + " from " + secondAnimal.getNameWithPrefix() + ".\n" +
                            "The sentence should satisfy one of the following templates:\n- It can ...\n- It has ...\n- It is a/an ...'\n");
                }

                String fact = scanner.nextLine();

                if (!fact.toLowerCase().startsWith("it can") && !fact.toLowerCase().startsWith("it is") && !fact.toLowerCase().startsWith("it has")) {
                    System.out.println("The examples of a statement:\n" +
                            "- It can fly\n"+
                            "- It has horn\n"+
                            "- It is a mammal\n"+
                            "Specify a fact that distinguishes " + animal.getNameWithPrefix() + " from " + secondAnimal.getNameWithPrefix() + ".\n" +
                            "The sentence should be of the format: 'It can/has/is ...'.");
                    fact = scanner.nextLine().replaceAll("\\p{P}$", "");
                }

                assert secondAnimal != null;
                System.out.println("Is the statement correct for " + secondAnimal.getNameWithPrefix() + "?");

                response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                String factWithoutPrefix = Arrays.stream(fact.split(" ")).skip(2).collect(Collectors.joining(" "));
                while (!aff_responses.contains(response) && !neg_responses.contains(response)) {
                    System.out.println(confirmations.get(random.nextInt(confirmations.size())));

                    response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                }
                secondTree.add(new String[]{factWithoutPrefix, fact}, "");

                if (aff_responses.contains(response)) {
                    animal.addFact(new String[]{factWithoutPrefix, fact.split(" ")[1], "no"});
                    secondAnimal.addFact(new String[]{factWithoutPrefix, fact.split(" ")[1], "yes"});
                    secondTree.add(animal, "NO");
                    secondTree.add(secondAnimal, "YES");
                } else {
                    animal.addFact(new String[]{factWithoutPrefix, fact.split(" ")[1], "yes"});
                    secondAnimal.addFact(new String[]{factWithoutPrefix, fact.split(" ")[1], "no"});
                    secondTree.add(animal, "YES");
                    secondTree.add(secondAnimal, "NO");
                }

                System.out.println("I have learned the following facts about animals:");
                secondTree.traversePreOrder(secondTree.root);

                System.out.println("I can distinguish these animals by asking the question:");
                System.out.println(getQuestion((String[])secondTree.root.value) + "?\n");

                System.out.println("Nice! I've learned so much about animals!\n");
            }

            System.out.println("Would you like to play again?");

            String response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
            while (!aff_responses.contains(response) && !neg_responses.contains(response)) {
                System.out.println(confirmations.get(random.nextInt(confirmations.size())));

                response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
            }

            if (neg_responses.contains(response)) {
                playing = false;
            }
        }

        System.out.println("\n" + bye_greetings[random.nextInt(bye_greetings.length)]);
    }

    private static Node playGame(Node parentNode, Node node, List<String> aff_responses, List<String> neg_responses, List<String> confirmations, Random random, Scanner scanner) {
        if (node != null) {
            if (node.value instanceof String[] && ((String[])node.value)[1].toLowerCase().startsWith("it")) {
                System.out.println(getQuestion((String[])node.value) + "?\n");
                String response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                while (!aff_responses.contains(response) && !neg_responses.contains(response)) {
                    System.out.println(confirmations.get(random.nextInt(confirmations.size())));

                    response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                }

                if (neg_responses.contains(response)) {
                    return playGame(node, node.left, aff_responses, neg_responses, confirmations, random, scanner);
                } else {
                    return playGame(node, node.right, aff_responses, neg_responses, confirmations, random, scanner);
                }

            } else {
                System.out.println("Is it " + ((Animal) node.value).getNameWithPrefix() + "?\n");
                String response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                while (!aff_responses.contains(response) && !neg_responses.contains(response)) {
                    System.out.println(confirmations.get(random.nextInt(confirmations.size())));

                    response = scanner.nextLine().toLowerCase().replaceAll("\\p{P}$", "").replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                }

                if (aff_responses.contains(response)) {
                    return new Node(new String[]{"found"});
                } else {
                    return node;
                }
            }

        } else {
            return parentNode;
        }
    }

    public static String getPrefix(String[] fact) {
        String[] pos = {"can", "has", "is"};
        String[] neg = {"can't", "doesn't have", "isn't"};

        if (fact[2].equals("no")) {
            for (int i = 0; i < pos.length; i++) {
                fact[1] = fact[1].replace(pos[i], neg[i]);
            }
        } else {
            for (int i = 0; i < pos.length; i++) {
                fact[1] = fact[1].replace(neg[i], pos[i]);
            }
        }

        return fact[1] + " " + fact[0];
    }

    public static String getQuestion(String[] fact) {
        String[] neg = {"can't", "doesn't have", "isn't"};
        String[] pos = {"can", "has", "is"};
        String[] quest = {"Can it ", "Does it have ", "Is it "};


        for (int i = 0; i < pos.length; i++) {
            if (fact[1].contains(pos[i])) {
                return quest[i] + fact[0];
            }
        }

        for (int i = 0; i < neg.length; i++) {
            if (fact[1].contains(neg[i])) {
                return quest[i] + fact[0];
            }
        }
        return "";
    }

    private static String[] identifyTheAnimal(Scanner scanner) {
        String animal = scanner.nextLine().toLowerCase();

        return getAnimalWithAndWithoutPrefix(animal);
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


