package org.openstack.console.commands;

import java.util.List;

import org.kohsuke.args4j.Argument;
import org.openstack.api.images.ImagesResource;
import org.openstack.console.OpenstackCliContext;
import org.openstack.model.images.Image;
import org.openstack.model.images.glance.GlanceImage;

import com.google.common.base.Strings;

public class CreateImage extends OpenstackCliCommandRunnerBase {
	@Argument(index = 0)
	public String name;

	@Argument(index = 1, multiValued = true)
	public List<String> properties;

	public CreateImage() {
		super("create", "image");
	}

	@Override
	public Object runCommand() throws Exception {
		if (Strings.isNullOrEmpty(name)) {
			throw new IllegalArgumentException("Name is required");
		}

		OpenstackCliContext context = getContext();

		ImagesResource imageClient = context.getImageClient();

		GlanceImage imageTemplate = new GlanceImage();
		imageTemplate.setName(name);

		if (properties != null) {
			for (String property : properties) {
				int equalsIndex = property.indexOf('=');
				if (equalsIndex == -1) {
					throw new IllegalArgumentException("Can't parse: " + property);
				}

				String key = property.substring(0, equalsIndex);
				String value = property.substring(equalsIndex + 1);

				imageTemplate.getProperties().put(key, value);
			}
		}

		// os create-image ImageFactory-bootstrap is_public=True disk_format=qcow2
		// system_id="http://org.platformlayer/service/imagefactory/v1.0:bootstrap" container_format=bare < disk.qcow2

		// This command will probably be faster _not_ in nailgun mode
		// InputStream imageStream = new NoCloseInputStream(System.in);

		Image image = imageClient.post(System.in, -1, imageTemplate);

		return image;
	}

}
