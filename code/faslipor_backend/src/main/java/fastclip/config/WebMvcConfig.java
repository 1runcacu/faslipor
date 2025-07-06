package fastclip.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    //配置静态资源映射
 @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry){
     log.info("开始静态资源映射");
     registry.addResourceHandler("/Room/**").addResourceLocations("classpath:/Room/");
     //registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
 }

}
