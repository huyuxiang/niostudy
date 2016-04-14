package daily.template.rpc.rpcf.spring.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import daily.template.rpc.rpcf.spring.ReferenceBean;

public class RpcfNamespaceHandler extends NamespaceHandlerSupport {
	
	public void init() {
		registerBeanDefinitionParser("reference", new RpcfBeanDefinationParser(ReferenceBean.class, false));
	}
}
