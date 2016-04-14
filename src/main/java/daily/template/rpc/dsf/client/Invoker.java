package daily.template.rpc.dsf.client;

import java.lang.reflect.Method;

import daily.template.rpc.dsf.common.RpcException;

/**
 * 动态代理Invoker
 * @author ZhongweiLee
 *
 */
public interface Invoker {
	
	 Object invoke(Method method, Object[] args) throws RpcException;

}
