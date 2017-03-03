package configuration;

import net.catten.ssim.repositories.LessonDAO;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by catten on 3/4/17
 */
@Configuration
public class TestConfiguration {
    @Bean
    public LessonDAO lessonDAO(){
        return Mockito.mock(LessonDAO.class);
    }
}
