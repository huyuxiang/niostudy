package daily.template.rpc.dsf.registry;

public interface IRegistry {
	
	void register(String value) throws Exception;
	
	void unregister();
	
	DynamicHostSet findAllService();

}
