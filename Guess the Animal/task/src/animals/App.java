package animals;

import java.util.ListResourceBundle;
import java.util.function.UnaryOperator;

public class App extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][]{
            {"morning_greetings", new String[]{"Good morning!"}},
            {"afternoon_greetings", new String[]{"Good afternoon!"}},
            {"evening_greetings", new String[]{"Good evening!"}},
            {"bye_greetings", new String[]{
                    "Bye!",
                    "Bye, bye!",
                    "See you later!",
                    "See you soon!",
                    "Have a nice one!"
            }},
            {"animal.name", (UnaryOperator<String>) name -> {
                if (name.matches("[aeiou].*")) {
                    return "an " + name;
                } else {
                    return "a " + name;
                }
            }},
            {"animal.question", (UnaryOperator<String>) animal -> "Is it " + animal + "?"},
            {"start_game", "I want to learn about animals.\nWhich animal do you like most?"},
            {"start_menu", "\nWelcome to the animal expert system!\n"},
            {"print_menu", """
                            What do you want to do:
                            
                            1. Play the guessing game
                            2. List of all animals
                            3. Search for an animal
                            4. Calculate statistics
                            5. Print the Knowledge Tree
                            0. Exit"""},
            {"play_game", "You think of an animal, and I guess it.\nPress enter when you're ready."},
            {"give_up", "I give up. What animal do you have in mind?"},
            {"specify_fact", (UnaryOperator<String[]>) animals -> new String[]{"Specify a fact that distinguishes " + animals[0] + " from " + animals[1] + ".\nThe sentence should satisfy one of the following templates:\n- It can ...\n- It has ...\n- It is a/an ...'\n"}},
            {"fact_example", "The examples of a statement:\n- It can fly\n- It has horn\n- It is a mammal\n"},
            {"fact_confirmation", (UnaryOperator<String>) animal -> "Is the statement correct for " + animal + "?"},
            {"learned", "I have learned the following facts about animals:"},
            {"distinguish", "I can distinguish these animals by asking the question:"},
            {"learned_so_much", "I've learned so much about animals!\n"},
            {"play_again", "Would you like to play again?"},
            {"start_new_game", "Wonderful!\nLet's play a game!\nYou think of an animal, and I guess it.\nPress enter when you're ready."},
            {"insert_animal", "Enter the animal:"},
            {"known_facts", (UnaryOperator<String>) animal -> "Facts about " + animal + ":"},
            {"known_no_facts", (UnaryOperator<String>) animal -> "No facts about " + animal},
            {"knowledge_stats", "The Knowledge Tree stats\n"},
            {"stats_root_node", "- root node "},
            {"stats_num_nodes", "- total number of nodes "},
            {"stats_num_animals", "- total number of animals "},
            {"stats_num_statements", "- total number of statements "},
            {"stats_height", "- height of the tree "},
            {"stats_min", "- minimum animal's depth "},
            {"stats_max", "- max animal's depth "},
            {"stats_avg", "- average animal's depth "}
       };
    }
}