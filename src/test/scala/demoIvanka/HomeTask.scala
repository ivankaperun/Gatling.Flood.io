package demoIvanka


import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

class HomeTask extends Simulation {

	val th_min = 1
	val th_max = 2
	val environment: String = System.getProperty("apiURL", "https://challenge.flood.io")
	val test_duration: Integer = Integer.getInteger("duration", 60)
	val test_users: Integer = Integer.getInteger("users", 5)

	val httpProtocol: HttpProtocolBuilder = http
		.baseUrl(environment)
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*css.*""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("uk-UA,uk;q=0.8,en-US;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:97.0) Gecko/20100101 Firefox/97.0")
		.disableFollowRedirect

	val headers_0 = Map(
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "none",
		"Sec-Fetch-User" -> "?1",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map(
		"Origin" -> "https://challenge.flood.io",
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "same-origin",
		"Sec-Fetch-User" -> "?1",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "same-origin",
		"Sec-Fetch-User" -> "?1",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_5 = Map(
		"Accept" -> "*/*",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-origin",
		"X-Requested-With" -> "XMLHttpRequest")

	val headers_7 = Map(
		"Accept" -> "text/html, application/xhtml+xml",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-origin",
		"Turbolinks-Referrer" -> "https://challenge.flood.io/done")


	val scn: ScenarioBuilder = scenario("HomeTask")
		.during(test_duration) {
			exec(http("Open Homepage")
				.get("/")
				.headers(headers_0)
				.check(status.is(200), status.not(404))
				.check(regex("""<title>(.+?)</title>""").is("Flood IO Script Challenge"))
				.check(substring("Welcome to our Script Challenge").find.exists)
				.check(css("#new_challenger > div > input[type=hidden]:nth-child(2)", "value").saveAs("authenticity_token"))
				.check(css("#challenger_step_id", "value").saveAs("challenger_step_id"))
				.check(css("#challenger_step_number", "value").saveAs("step_number"))
			)
				.exec(
					session => {
						println("[INFO] AUTHENTICITY TOKEN")
						println(session("authenticity_token").as[String])
						println("[INFO] CHALLENGER STEP ID")
						println(session("challenger_step_id").as[String])
						session
					}
				)
				.pause(th_min, th_max)
				.exec(http("Click Button Start")
					.post("/start")
					.headers(headers_1)
					.formParam("utf8", "✓")
					.formParam("authenticity_token", "${authenticity_token}")
					.formParam("challenger[step_id]", "${challenger_step_id}")
					.formParam("challenger[step_number]", "${step_number}")
					.formParam("commit", "Start")
					.check(status.is(302)))
				.pause(th_min, th_max)
				.exec(http("Select Age")
					.get("/step/2")
					.headers(headers_0)
					.check(status.is(200), status.not(404))
					.check(substring("Step 2").find.exists)
					.check(css("#challenger_step_id", "value").saveAs("challenger_step_id_1"))
					.check(css("#challenger_step_number", "value").saveAs("step_number_1"))
					.check(css("#challenger_age > option:nth-child(14)", "value").saveAs("age"))
				)
				.exec(http("Click Button Next")
					.post("/start")
					.headers(headers_1)
					.formParam("utf8", "✓")
					.formParam("authenticity_token", "${authenticity_token}")
					.formParam("challenger[step_id]", "${challenger_step_id_1}")
					.formParam("challenger[step_number]", "${step_number_1}")
					.formParam("challenger[age]", "${age}")
					.formParam("commit", "Next")
					.check(status.is(302)))
				.pause(th_min, th_max)
				.exec(http("Select Order With Max Value")
					.get("/step/3")
					.headers(headers_2)
					.check(status.is(200), status.not(404))
					.check(substring("Step 3").find.exists)
					.check(css("#challenger_step_id", "value").saveAs("challenger_step_id_2"))
					.check(css("#challenger_step_number", "value").saveAs("step_number_2"))
					.check(css("label.collection_radio_buttons").findAll.transform(list => list.map(_.toInt).max).saveAs("largest_order"))
					.check(regex("class=\"collection_radio_buttons\" for=\"(.*?)\">${largest_order}").find.saveAs("challenger_order_selected"))
					.check(css("#${challenger_order_selected}", "value").saveAs("order_selected"))
				)
				.exec(http("Click Button Next")
					.post("/start")
					.headers(headers_1)
					.formParam("utf8", "✓")
					.formParam("authenticity_token", "${authenticity_token}")
					.formParam("challenger[step_id]", "${challenger_step_id_2}")
					.formParam("challenger[step_number]", "${step_number_2}")
					.formParam("challenger[largest_order]", "${largest_order}")
					.formParam("challenger[order_selected]", "${order_selected}")
					.formParam("commit", "Next")
					.check(status.is(302)))
				.pause(th_min, th_max)
				.exec(http("Proceed To Next Step")
					.get("/step/4")
					.headers(headers_2)
					.check(status.is(200), status.not(404))
					.check(substring("This step is easy!").find.exists)
					.check(css("#challenger_step_id", "value").saveAs("challenger_step_id_3"))
					.check(css("#challenger_step_number", "value").saveAs("step_number_3"))
					.check(css("#new_challenger > input:nth-child(5)", "name").saveAs("order_name_1"))
					.check(css("#new_challenger > input:nth-child(5)", "value").saveAs("order_value_1"))
					.check(css("#new_challenger > input:nth-child(6)", "name").saveAs("order_name_2"))
					.check(css("#new_challenger > input:nth-child(6)", "value").saveAs("order_value_2"))
					.check(css("#new_challenger > input:nth-child(7)", "name").saveAs("order_name_3"))
					.check(css("#new_challenger > input:nth-child(7)", "value").saveAs("order_value_3"))
					.check(css("#new_challenger > input:nth-child(8)", "name").saveAs("order_name_4"))
					.check(css("#new_challenger > input:nth-child(8)", "value").saveAs("order_value_4"))
					.check(css("#new_challenger > input:nth-child(9)", "name").saveAs("order_name_5"))
					.check(css("#new_challenger > input:nth-child(9)", "value").saveAs("order_value_5"))
					.check(css("#new_challenger > input:nth-child(10)", "name").saveAs("order_name_6"))
					.check(css("#new_challenger > input:nth-child(10)", "value").saveAs("order_value_6"))
					.check(css("#new_challenger > input:nth-child(11)", "name").saveAs("order_name_7"))
					.check(css("#new_challenger > input:nth-child(11)", "value").saveAs("order_value_7"))
					.check(css("#new_challenger > input:nth-child(12)", "name").saveAs("order_name_8"))
					.check(css("#new_challenger > input:nth-child(12)", "value").saveAs("order_value_8"))
					.check(css("#new_challenger > input:nth-child(13)", "name").saveAs("order_name_9"))
					.check(css("#new_challenger > input:nth-child(13)", "value").saveAs("order_value_9"))
					.check(css("#new_challenger > input:nth-child(14)", "name").saveAs("order_name_10"))
					.check(css("#new_challenger > input:nth-child(14)", "value").saveAs("order_value_10"))
				)
				.exec(http("Click Button Next")
					.post("/start")
					.headers(headers_1)
					.formParam("utf8", "✓")
					.formParam("authenticity_token", "${authenticity_token}")
					.formParam("challenger[step_id]", "${challenger_step_id_3}")
					.formParam("challenger[step_number]", "${step_number_3}")
					.formParam("${order_name_1}", "${order_value_1}")
					.formParam("${order_name_2}", "${order_value_2}")
					.formParam("${order_name_3}", "${order_value_3}")
					.formParam("${order_name_4}", "${order_value_4}")
					.formParam("${order_name_5}", "${order_value_5}")
					.formParam("${order_name_6}", "${order_value_6}")
					.formParam("${order_name_7}", "${order_value_7}")
					.formParam("${order_name_8}", "${order_value_8}")
					.formParam("${order_name_9}", "${order_value_9}")
					.formParam("${order_name_10}", "${order_value_10}")
					.formParam("commit", "Next")
					.check(status.is(302))
					.resources(http("Get Spec")
						.get("/code")
						.headers(headers_5)))
				.pause(th_min, th_max)
				.exec(http("Get One Time Token")
					.get("/step/5")
					.headers(headers_2)
					.check(status.is(200), status.not(404))
					.check(substring("Step 5").find.exists)
					.check(css("#challenger_step_id", "value").saveAs("challenger_step_id_4"))
					.check(css("#challenger_step_number", "value").saveAs("step_number_4"))
					.check(css("span.token").saveAs("one_time_token"))
				)
				.exec(http("Post One Time Token")
					.post("/start")
					.headers(headers_1)
					.formParam("utf8", "✓")
					.formParam("authenticity_token", "${authenticity_token}")
					.formParam("challenger[step_id]", "${challenger_step_id_4}")
					.formParam("challenger[step_number]", "${step_number_4}")
					.formParam("challenger[one_time_token]", "${one_time_token}")
					.formParam("commit", "Next")
					.check(status.is(302)))
				.pause(th_min, th_max)
				.exec(http("Get Done")
					.get("/done")
					.headers(headers_2)
					.check(status.is(200), status.not(404))
					.check(substring("You're Done!").find.exists)
				)
				.exec(http("Click Start Again")
					.get("/")
					.headers(headers_7)
					.check(status.is(200), status.not(404)))
		}

	setUp(
		scn.inject(
			rampUsers(test_users).during(test_duration)
		)).assertions(
				global.responseTime.max.lt(1000),
				global.responseTime.percentile3.lt(900),
				global.successfulRequests.percent.gt(90)
		).protocols(httpProtocol)
}