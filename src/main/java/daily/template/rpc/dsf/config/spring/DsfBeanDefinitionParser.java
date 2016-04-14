package daily.template.rpc.dsf.config.spring;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Spring schema 配置解析器 <dsf:xxxxx .../>
 * @author ZhongweiLee
 *
 */
public class DsfBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	private final Class<?> beanClass;

	public DsfBeanDefinitionParser(Class<?> beanClass) {

		this.beanClass = beanClass;
	}

	@Override
	protected Class<?> getBeanClass(Element element) {
		return beanClass;
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext,
			BeanDefinitionBuilder builder) {

		BeanDefinition beanDefinition = builder.getBeanDefinition();

		// 根据Bean的set方法名称查找配置文件中相应属性值，进行set操作
		Method[] methods = getBeanClass(element).getMethods();
		String id = StringUtils.EMPTY;
		for (Method method : methods) {
			if (method.getName().length() > 3
					&& method.getName().startsWith("set")
					&& method.getParameterTypes().length == 1) {
				// setXxxx方法只有一个参数
				// 根据set方法名称，依据驼峰命名规则，得到属性名称 setId(String id) --> 得到有id属性
				String attribute = method.getName().substring(3);
				char ch = attribute.charAt(0);
				attribute = Character.toLowerCase(ch) + attribute.substring(1);

				String value = element.getAttribute(attribute); // 配置文件中设置的属性值

				if (StringUtils.isNotEmpty(value)) {
					Type type = method.getParameterTypes()[0];
					if (type == boolean.class) {
						beanDefinition.getPropertyValues().addPropertyValue(attribute, Boolean.valueOf(value));
					} else {
	
						if ("ref".equals(attribute)&& parserContext.getRegistry().containsBeanDefinition(value)) {
							beanDefinition.getPropertyValues().addPropertyValue(attribute, new RuntimeBeanReference(value));
						} else {
							beanDefinition.getPropertyValues().addPropertyValue(attribute, value);
							if ("id".equals(attribute)) {
								id = value;
							}
						}
					}
				}

			}

		}
		
		parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);

	}
}
