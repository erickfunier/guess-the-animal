package animals;

import java.util.ListResourceBundle;
import java.util.function.UnaryOperator;

public class App_eo extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][]{
            {"morning_greetings", new String[]{"Bonan matenon!"}},
            {"afternoon_greetings", new String[]{"Bonan posttagmezon!"}},
            {"evening_greetings", new String[]{"Bonan vesperon!"}},
            {"bye_greetings", new String[]{
                    "Ĝis!",
                    "Ĝis revido!",
                    "Estis agrable vidi vin!"
            }},
            {"animal.name", (UnaryOperator<String>) name -> name},
            {"animal.question", (UnaryOperator<String>) animal ->
                    "Ĉu ĝi estas " + animal + "?"},
            {"start_game", "Mi volas lerni pri bestoj.\nKiun beston vi plej ŝatas?"},
            {"start_menu", "\nBonvenon al la sperta sistemo de la besto!\n"},
            {"print_menu", """
                        Kion vi volas fari:
                        
                        1. Ludi la divenludon
                        2. Listo de ĉiuj bestoj
                        3. Serĉi beston
                        4. Kalkuli statistikon
                        5. Presu la Scion-Arbon
                        0. Eliri"""},
            {"play_game", "Vi pensu pri besto, kaj mi divenos ĝin.\nPremu enen kiam vi pretas."},
            {"give_up", "Mi rezignas. Kiun beston vi havas en la kapo?"},
            {"specify_fact", (UnaryOperator<String[]>) animals -> new String[]{"Indiku fakton, kiu distingas " + animals[0] + " de " + animals[1] + ".\nLa frazo devas kontentigi unu el la jenaj ŝablonoj:\n- Ĝi povas ...\n- Ĝi havas ...\n- Ĝi estas a/an ...'\n"}},
            {"fact_example", "Ekzemploj de deklaro:\n- Ĝi povas flugi\n- Ĝi havas kornon\n- Ĝi estas mamulo\n"},
            {"fact_confirmation", (UnaryOperator<String>) animal -> "Ĉu la aserto ĝustas por la " + animal + "?"},
            {"learned", "Mi lernis la jenajn faktojn pri bestoj:"},
            {"distinguish", "Mi povas distingi ĉi tiujn bestojn farante la demandon:"},
            {"learned_so_much", "Mi multe lernis pri bestoj!\n"},
            {"play_again", "Ĉu vi ŝatus ludi denove?"},
            {"start_new_game", "Mirinda!\nNi ludu ludon!\nVi pensas pri besto, kaj mi supozas ĝin.\nPremu enen kiam vi estas preta."},
            {"insert_animal", "Enigu la beston:"},
            {"known_facts", (UnaryOperator<String>) animal -> "Faktoj pri " + animal + ":"},
            {"known_no_facts", (UnaryOperator<String>) animal -> "Neniuj faktoj pri " + animal},
            {"knowledge_stats", "La statistiko de la Scio-Arbo\n"},
            {"stats_root_node", "- radika nodo "},
            {"stats_num_nodes", "- totala nombro de nodoj "},
            {"stats_num_animals", "- totala nombro de bestoj "},
            {"stats_num_statements", "- totala nombro de deklaroj "},
            {"stats_height", "- alteco de la arbo "},
            {"stats_min", "- minimuma profundo de besto "},
            {"stats_max", "- maksimuma profundo de besto "},
            {"stats_avg", "- averaĝa profundo de besto "}
       };
    }
}
