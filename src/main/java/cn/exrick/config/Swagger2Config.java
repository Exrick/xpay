package cn.exrick.config;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Exrickx
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    private Logger log = LoggerFactory.getLogger(Swagger2Config.class);

    @Bean
    public Docket createRestApi() {

        log.info("开始加载Swagger2...");

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).select()
                //扫描所有有注解的api，用这种方式更灵活
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("XPay Api Documentation")
                .description("XPay个人支付收款系统API接口文档")
                .termsOfServiceUrl("http://xpay.exrick.cn/")
                .contact(new Contact("Exrick", "http://blog.exrick.cn", "1012139570@qq.com"))
                .version("1.0.0")
                .build();
    }
}
