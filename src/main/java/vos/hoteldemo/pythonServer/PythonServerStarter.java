package vos.hoteldemo.pythonServer;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class PythonServerStarter {
    @PostConstruct
    public void startPythonServer() {
        try {
            String pathToPythonScript = new ClassPathResource("static/pythonServer/faceRecognition.py").getFile().getAbsolutePath();
            ProcessBuilder processBuilder = new ProcessBuilder("python", pathToPythonScript);
            processBuilder.redirectErrorStream(true);
            processBuilder.start();
            System.out.println("✅ Flask AI server started: " + pathToPythonScript);
        } catch (IOException e) {
            System.err.println("❌ Không thể khởi động Flask server: " + e.getMessage());
        }
    }
}


