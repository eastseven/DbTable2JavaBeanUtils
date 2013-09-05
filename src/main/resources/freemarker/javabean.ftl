package ${package}

public class ${className} {

	<#list properties as property>
	private ${property.type} ${property.name};
	</#list>

	<#list methods as method>
	public void ${method.set}(${method.type} ${method.name}) {
		this.${method.name} = ${method.name};
	}
	
	public ${method.type} ${method.get}() {
		return this.${method.name};
	}
	</#list>
}
