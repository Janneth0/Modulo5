package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


@SpringBootApplication
public class  SalvoApplication {


	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository,
									  GameRepository gameRepository,
									  GamePlayerRepository gamePlayerRepository,
									  ShipRepository shipRepository,
									  SalvoRepository salvoRepository,
									  ScoreRepository scoreRepository) {
		return (args) -> {
			////DATOS DE PLAYER
			Player p1 = new Player("j.bauer@ctu.gov", passwordEncoder().encode("24"));
			Player p2 = new Player("c.obrian@ctu.gov", passwordEncoder().encode("42"));
			Player p3 = new Player("kim_bauer@gmail.com", passwordEncoder().encode("kb"));
			Player p4 = new Player("t.almeida@ctu.gov", passwordEncoder().encode("mole"));

			playerRepository.saveAll(Arrays.asList(p1, p2, p3, p4));

			/*
			* $.post("/api/login", { username: "j.bauer@ctu.gov", password: "24" }).done(function() { console.log("logged in!"); })
			* $.post("/api/logout").done(function() { console.log("logged out"); })
			* */


			////DATOS DE GAME
			Date date = new Date();
			Date date2 = Date.from(date.toInstant().plusSeconds(3600));
			Date date3 = Date.from(date2.toInstant().plusSeconds(3600));
			Date date4 = Date.from(date3.toInstant().plusSeconds(3600));
			Date date5 = Date.from(date4.toInstant().plusSeconds(3600));
			Date date6 = Date.from(date5.toInstant().plusSeconds(3600));
			Date date7 = Date.from(date6.toInstant().plusSeconds(3600));
			Date date8 = Date.from(date7.toInstant().plusSeconds(3600));

			Game g1 = new Game(date);
			Game g2 = new Game(date2);
			Game g3 = new Game(date3);
			Game g4 = new Game(date4);
			Game g5 = new Game(date5);
			Game g6 = new Game(date6);
			Game g7 = new Game(date7);
			Game g8 = new Game(date8);

			gameRepository.saveAll(Arrays.asList(g1, g2, g3, g4, g5, g6, g7, g8));

			///DATOS DE GAMEPLAYER
			//Game1
			GamePlayer gp1 = new GamePlayer(g1, p1);
			GamePlayer gp2 = new GamePlayer(g1, p2);

			//Game2
			GamePlayer gp3 = new GamePlayer(g2, p1);
			GamePlayer gp4 = new GamePlayer(g2, p2);

			//Game3
			GamePlayer gp5 = new GamePlayer(g3, p2);
			GamePlayer gp6 = new GamePlayer(g3, p4);

			//Game4
			GamePlayer gp7 = new GamePlayer(g4, p2);
			GamePlayer gp8 = new GamePlayer(g4, p3);

			//Game5-------------------------
			GamePlayer gp9 = new GamePlayer(g5, p4);
			GamePlayer gp10 = new GamePlayer(g5, p1);

			//Game6-------------------
			GamePlayer gp11 = new GamePlayer(g6, p3);
			//GamePlayer gp12 = new GamePlayer(date,g6);

			//Game7---------------
			GamePlayer gp13 = new GamePlayer(g7, p4);
			//GamePlayer gp14 = new GamePlayer(date,g7,);

			//Game8--------
			GamePlayer gp15 = new GamePlayer(g8, p3);
			GamePlayer gp16 = new GamePlayer(g8, p4);

			gamePlayerRepository.saveAll(Arrays.asList(gp1, gp2, gp3, gp4, gp5, gp6, gp7, gp8, gp9, gp10, gp11, gp13, gp15, gp16));
			//gamePlayerRepository.save(gp12);
			//gamePlayerRepository.save(gp14);

			///DATOS DE SHIP				Ship ship22=new Ship("Destroyer",gp11,location22);
			///DATOS DE SHIP
			//asignar datos

			//game1
			Ship ship1 = new Ship("Destroyer", gp1, new ArrayList<>(Arrays.asList("H2", "H3", "H4")));
			Ship ship2 = new Ship("Submarine", gp1, new ArrayList<>(Arrays.asList("E1", "F1", "G1")));
			Ship ship3 = new Ship("Patrol Boat", gp1, new ArrayList<>(Arrays.asList("B4","B5")));
			Ship ship4 = new Ship("Destroyer", gp2,  new ArrayList<>(Arrays.asList("B5", "C5", "D5")));
			Ship ship5 = new Ship("Patrol Boat", gp2, new ArrayList<>(Arrays.asList("F1", "F2")));
			//game2
			Ship ship6 = new Ship("Destroyer", gp3, new ArrayList<>(Arrays.asList("B5", "C5", "D5")));
			Ship ship7 = new Ship("Patrol Boat", gp3, new ArrayList<>(Arrays.asList("C6", "C7")));
			Ship ship8 = new Ship("Submarine", gp4, new ArrayList<>(Arrays.asList("A2", "A3", "A4")));
			Ship ship9 = new Ship("Patrol Boat", gp4,  new ArrayList<>(Arrays.asList("G6", "H6")));
			//game3
			Ship ship10 = new Ship("Destroyer", gp5, new ArrayList<>(Arrays.asList("B5", "C5", "D5")));
			Ship ship11 = new Ship("Patrol Boat", gp3, new ArrayList<>(Arrays.asList("C6", "C7")));
			Ship ship12 = new Ship("Submarine", gp4, new ArrayList<>(Arrays.asList("A2", "A3", "A4")));
			Ship ship13 = new Ship("Patrol Boat", gp4,  new ArrayList<>(Arrays.asList("G6", "H6")));
			//game4
			Ship ship14 = new Ship("Destroyer", gp3,  new ArrayList<>(Arrays.asList("B5", "C5", "D5")));
			Ship ship15 = new Ship("Patrol Boat", gp3, new ArrayList<>(Arrays.asList("C6", "C7")));
			Ship ship16 = new Ship("Submarine", gp4, new ArrayList<>(Arrays.asList("A2", "A3", "A4")));
			Ship ship17 = new Ship("Patrol Boat", gp4,  new ArrayList<>(Arrays.asList("G6", "H6")));
			//game5
			Ship ship18 = new Ship("Destroyer", gp3,  new ArrayList<>(Arrays.asList("B5", "C5", "D5")));
			Ship ship19 = new Ship("Patrol Boat", gp3, new ArrayList<>(Arrays.asList("C6", "C7")));
			Ship ship20 = new Ship("Submarine", gp4, new ArrayList<>(Arrays.asList("A2", "A3", "A4")));
			Ship ship21 = new Ship("Patrol Boat", gp4, new ArrayList<>(Arrays.asList("G6", "H6")));
			//game6
			Ship ship22 = new Ship("Destroyer", gp3, new ArrayList<>(Arrays.asList("B5", "C5", "D5")));
			Ship ship23 = new Ship("Patrol Boat", gp3,  new ArrayList<>(Arrays.asList("C6", "C7")));
			//game8
			Ship ship24 = new Ship("Destroyer", gp3, new ArrayList<>(Arrays.asList("B5", "C5", "D5")));
			Ship ship25 = new Ship("Patrol Boat", gp3, new ArrayList<>(Arrays.asList("C6", "C7")));
			Ship ship26 = new Ship("Submarine", gp4, new ArrayList<>(Arrays.asList("A2", "A3", "A4")));
			Ship ship27 = new Ship("Patrol Boat", gp4, new ArrayList<>(Arrays.asList("G6", "H6")));

			shipRepository.saveAll(Arrays.asList(ship1, ship2, ship3, ship4, ship4, ship5, ship6, ship7, ship8, ship9, ship10, ship11, ship12, ship13, ship14, ship15, ship16, ship17, ship18, ship19, ship20, ship21, ship22, ship23, ship24, ship25, ship26, ship27));

			///DATOS DE SALVO
			Salvo salvo1 = new Salvo(gp1, 1, new HashSet<>(Arrays.asList("B5", "C5", "F1")));
			Salvo salvo2 = new Salvo(gp2, 1, new HashSet<>(Arrays.asList("B4", "B5", "B6")));
			Salvo salvo3 = new Salvo(gp1, 2, new HashSet<>(Arrays.asList("F2", "D5")));
			Salvo salvo4 = new Salvo(gp2, 2, new HashSet<>(Arrays.asList("E1", "H3", "A2")));
			Salvo salvo5 = new Salvo(gp3, 1, new HashSet<>(Arrays.asList("A2", "A4", "G6")));
			Salvo salvo6 = new Salvo(gp4, 1, new HashSet<>(Arrays.asList("B5", "D5", "C7")));
			Salvo salvo7 = new Salvo(gp3, 1,new HashSet<>(Arrays.asList("A3", "H6")));
			Salvo salvo8 = new Salvo(gp4, 1, new HashSet<>(Arrays.asList("C5", "C6")));
			Salvo salvo9 = new Salvo(gp5, 1, new HashSet<>(Arrays.asList("G6", "H6", "A4")));
			Salvo salvo10 = new Salvo(gp6, 1,new HashSet<>(Arrays.asList("H1", "H2", "H3")));
			Salvo salvo11 = new Salvo(gp5, 1, new HashSet<>(Arrays.asList("A2", "A3", "D8")));
			Salvo salvo12 = new Salvo(gp6, 1, new HashSet<>(Arrays.asList("E1", "F2", "G3")));
			Salvo salvo13 = new Salvo(gp7, 1, new HashSet<>(Arrays.asList("A3", "A4", "F7")));
			Salvo salvo14 = new Salvo(gp8, 1, new HashSet<>(Arrays.asList("B5", "C6", "H1")));
			Salvo salvo15 = new Salvo(gp7, 1, new HashSet<>(Arrays.asList("A2", "G6", "H6")));
			Salvo salvo16 = new Salvo(gp8, 1, new HashSet<>(Arrays.asList("C5", "C7", "D5")));
			Salvo salvo17 = new Salvo(gp9, 1, new HashSet<>(Arrays.asList("A1", "A2", "A3")));
			Salvo salvo18 = new Salvo(gp10, 1, new HashSet<>(Arrays.asList("B5", "B6", "C7")));
			Salvo salvo19 = new Salvo(gp9, 1, new HashSet<>(Arrays.asList("G6", "G7", "G8")));
			Salvo salvo20 = new Salvo(gp10, 1, new HashSet<>(Arrays.asList("C6", "D6", "E6")));
			Salvo salvo21 = new Salvo(gp10, 1, new HashSet<>(Arrays.asList("H1", "H8")));

			salvoRepository.saveAll(Arrays.asList(salvo1, salvo2, salvo3, salvo4, salvo5, salvo6, salvo7, salvo8, salvo9, salvo10, salvo11, salvo12, salvo13, salvo14, salvo15, salvo16, salvo17, salvo18, salvo19, salvo20, salvo21));

			///DATOS DE SCORE
			Score score1 = new Score((float) 0.5, date, p1, g1);
			Score score2 = new Score((float) 0.5, date, p2, g1);
			Score score3 = new Score((float) 1, date, p3, g2);
			Score score4 = new Score((float) 0, date, p4, g2);

			scoreRepository.saveAll(Arrays.asList(score1, score2, score3, score4));
		};

	}

}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player player = playerRepository.findByUserName(inputName).get();
			if (player != null) {
				return new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/login").permitAll()
				.antMatchers("/web/games.html").permitAll()
				.antMatchers("/web/**").permitAll()
				.antMatchers("/api/games").permitAll()
				.antMatchers("/api/players").permitAll()
				.antMatchers("/api/game_view/*").hasAuthority("USER")
				.antMatchers("/rest").denyAll()
				.anyRequest().permitAll();


		http.formLogin()
				.usernameParameter("username")
				.passwordParameter("password")
				.loginPage("/api/login");
		http.logout().logoutUrl("/api/logout");


		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}
}