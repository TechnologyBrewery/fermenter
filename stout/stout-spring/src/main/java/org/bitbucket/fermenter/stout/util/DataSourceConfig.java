package org.bitbucket.fermenter.stout.util;

import org.aeonbits.owner.KrauseningConfig;
import org.aeonbits.owner.KrauseningConfig.KrauseningSources;;

@KrauseningSources("data-source.properties")
public interface DataSourceConfig extends KrauseningConfig {
	
	@Key("url")
	String getUrl();
	
}
