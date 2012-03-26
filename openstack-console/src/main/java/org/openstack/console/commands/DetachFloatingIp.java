package org.openstack.console.commands;

import org.kohsuke.args4j.Argument;
import org.openstack.api.compute.TenantResource;
import org.openstack.console.model.InstanceName;

public class DetachFloatingIp extends OpenstackCliCommandRunnerBase {
	@Argument(index = 0)
	public InstanceName instanceName;

	@Argument(index = 1)
	public String ip;

	public DetachFloatingIp() {
		super("detach", "floatingip");
	}

	@Override
	public Object runCommand() throws Exception {
		TenantResource compute = getContext().getComputeClient();

		String instanceId = instanceName.findInstanceId(getContext());

		compute.servers().server(instanceId).removeFloatingIp(ip);

		return ip;
	}
}
