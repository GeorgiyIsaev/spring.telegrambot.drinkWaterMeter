package spring.telegrambot.drinkWaterMeter.logger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import spring.telegrambot.drinkWaterMeter.client.contract.request.Request;


import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@Component
@ConditionalOnProperty(value="loggerEnable" ,havingValue="onlyNotebook",matchIfMissing = false)
public class ToNotebook implements Logger{
    private final LogFormater logFormater;
    private final Path pathLog;

    public ToNotebook(Path pathLog,
                                LogFormater logFormater) {
        this.logFormater = logFormater;
        this.pathLog = pathLog;
    }

    @Override
    public void logMessage(Update update){
        String text = logFormater.logMessage(update);
        this.write(text);
    }

    @Override
    public void logCallbackQuery(Update update){
        String text = logFormater.logCallbackQuery(update);
        this.write(text);
    }

    @Override
    public void logRequest(Request request){
        String text = logFormater.logRequest(request);
        this.write(text);
    }

    @Override
    public void logException(String info, Exception e){
        String text = logFormater.logException(info, e);
        this.write(text);
    }

    @Override
    public void logResponse(String response) {
        String text = logFormater.logResponse(response);
        this.write(text);
    }


    private void write(String textLog){
        //Запись текста в файл
        createDirectoriesIfNotExists(pathLog);
        try(FileWriter writer = new FileWriter(pathLog.toFile(), true))
        {
            writer.write(textLog);
            writer.write("\n");
            writer.flush(); //запись из буфера в файл
        }
        catch(IOException e){
            String text =  logFormater.logException("Невозможно осуществить сохранение лога в файл", e);
            System.out.println(text);
        }
    }

    private void createDirectoriesIfNotExists(Path userFile) {
        Path directory = userFile.getParent();
        if(directory != null && !Files.isDirectory(directory)){
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                String text =  logFormater.logException("Ошибка при создании каталога", e);
                System.out.println(text);
            }
        }
    }
}
