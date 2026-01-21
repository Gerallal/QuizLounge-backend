package gerallal.quizloungebackend.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class EvaluationService {

    private final OpenAIClient client = OpenAIOkHttpClient.fromEnv();


    public boolean evaluate(String givenAnswer, String answer) {
        String prompt = String.format("Wie ähnlich ist die Antwort '%s' zu der Antwort '%s'? Antworte nur true oder false", givenAnswer, answer);

        ResponseCreateParams params = ResponseCreateParams.builder()
                .input(prompt)
                .model(ChatModel.GPT_4_1_NANO)
                .build();

        Response response = client.responses().create(params);


        // Erster Output
        var outputItem = response.output().get(0);

        // Message extrahieren
        var message = outputItem.message().get();

        // Erster Content-Eintrag
        var content = message.content().get(0);

        // OutputText extrahieren (Optional!)
        var outputText = content.outputText().get();

        // Text auslesen
        String output = outputText.text().trim();


        System.out.println(output);

        return Boolean.parseBoolean(output);
    }
}
