package daily.template.rpc.dsf.config.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Spring schema 配置处理 <dfs:xxxx />
 * @author ZhongweiLee
 *
 */
public class DsfNamespaceHandler extends NamespaceHandlerSupport{

	@Override
	public void init() {
		
		registerBeanDefinitionParser("registry", new DsfBeanDefinitionParser(RegistryBean.class));
        registerBeanDefinitionParser("server", new DsfBeanDefinitionParser(ServerBean.class));
        registerBeanDefinitionParser("client", new DsfBeanDefinitionParser(ClientBean.class));
	}

}
