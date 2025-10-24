package ma.emsi.elfaize.tp2elfaize;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;

import java.util.Map;

public class Test2 {
    public static void main(String[] args) {

        // Clé API Gemini (définie comme variable d’environnement)
        String cle = System.getenv("GEMINI_KEY");

        // Création du modèle Gemini (même que Test1)
        ChatModel modele = GoogleAiGeminiChatModel.builder()
                .apiKey(cle)
                .modelName("gemini-2.5-flash")
                .temperature(0.7)
                .build();

        // Création d’un template pour un traducteur
        PromptTemplate template = PromptTemplate.from(
                "Traduis le texte suivant en anglais : {{texte}}"
        );

        // Valeur à insérer dans le template
        Prompt prompt = template.apply(Map.of("texte", "Bonjour, comment vas-tu aujourd’hui ?"));

        // Envoi du prompt au modèle
        String reponse = modele.chat(prompt.text());

        // Affichage de la réponse
        System.out.println("Prompt envoyé : " + prompt.text());
        System.out.println("Réponse du modèle : " + reponse);
    }
}

