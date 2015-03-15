package application;

import com.sun.jersey.api.client.Client;

import player.PlayerResource;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class FantasyFootballApplication extends
		Application<FantasyFootballConfiguration> {

	public static void main(String... args) throws Exception {
		new FantasyFootballApplication().run(args);
	}

	@Override
	public void initialize(Bootstrap<FantasyFootballConfiguration> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(FantasyFootballConfiguration configuration,
			Environment environment) throws Exception {
		Client client = new JerseyClientBuilder(environment).build(getName());
		environment.jersey().register(new PlayerResource(client));
	}

}
