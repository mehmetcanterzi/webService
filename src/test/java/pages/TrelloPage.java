/**
 * 
 */
package pages;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

/**
 * @author can.terzi
 *
 */
public class TrelloPage {

	final static Logger logger = LogManager.getLogger(TrelloPage.class);

	public String createBoard(String name) {
		HttpResponse<String> response = Unirest.post("https://api.trello.com/1/boards/")
				.queryString("key", "954f7852f6b177e16d5a42118cf67e22")
				.queryString("token", "c68855e9adcfb1a01885056d7cb31a419d30b84dc72851e2b654efa12ef969b2")
				.queryString("name", name).asString();

		String boardId = getFirstIdFromResponse(response.getBody().toString());
		logger.info("Board Created. Name : " + name + ", ID : " + boardId);
		return boardId;
	}

	public String createList(String name, String boardId) {
		HttpResponse<String> response = Unirest.post("https://api.trello.com/1/lists/")
				.queryString("key", "954f7852f6b177e16d5a42118cf67e22")
				.queryString("token", "c68855e9adcfb1a01885056d7cb31a419d30b84dc72851e2b654efa12ef969b2")
				.queryString("idBoard", boardId).queryString("name", name).asString();

		String listId = getFirstIdFromResponse(response.getBody().toString());
		logger.info("List Created. Name : " + name + ", ID : " + listId);
		return listId;

	}

	public ArrayList<String> getLists(String boardId) {
		HttpResponse<String> response = Unirest.get("https://api.trello.com/1/boards/" + boardId + "/lists")
				.queryString("key", "954f7852f6b177e16d5a42118cf67e22")
				.queryString("token", "c68855e9adcfb1a01885056d7cb31a419d30b84dc72851e2b654efa12ef969b2").asString();

		ArrayList<String> lists = getAllIdFromResponse(response.getBody().toString());

		System.out.println(response.getBody());
		logger.info("Lists size : " + lists.size() + " for board with ID : " + boardId);
		return lists;
	}

	public void checkCardAmount(String listId, int amount) {

		given().
			param("key", "954f7852f6b177e16d5a42118cf67e22").
			param("token", "c68855e9adcfb1a01885056d7cb31a419d30b84dc72851e2b654efa12ef969b2").
		when()
			.get("https://api.trello.com/1/lists/" + listId + "/cards").
		then().
			assertThat().
			body("id", hasSize(amount));
	}

	public void checkCardValue(String cardId, String key, String value) {

		given().
			param("key", "954f7852f6b177e16d5a42118cf67e22").
			param("token", "c68855e9adcfb1a01885056d7cb31a419d30b84dc72851e2b654efa12ef969b2").
		when().
			get("https://api.trello.com/1/cards/" + cardId).
		then().
			assertThat().
			body(key, equalTo(value));
	}

	public String createCard(String name, String listId) {

		HttpResponse<String> response = Unirest.post("https://api.trello.com/1/cards/")
				.queryString("key", "954f7852f6b177e16d5a42118cf67e22")
				.queryString("token", "c68855e9adcfb1a01885056d7cb31a419d30b84dc72851e2b654efa12ef969b2")
				.queryString("idList", listId).
				queryString("name", name).
				asString();

		String cardId = getFirstIdFromResponse(response.getBody().toString());
		logger.info("Card Created. Name : " + name + ", ID : " + cardId);
		return cardId;

	}

	public void updateCard(String cardId, String param, String value) {
		HttpResponse<JsonNode> response = Unirest.put("https://api.trello.com/1/cards/" + cardId)
				.header("Accept", "application/json")
				.queryString("key", "954f7852f6b177e16d5a42118cf67e22")
				.queryString("token", "c68855e9adcfb1a01885056d7cb31a419d30b84dc72851e2b654efa12ef969b2")
				.queryString(param, value).
				asJson();

		response.getBody();
		logger.info("Card Updated. Card ID : " + cardId + ", Parameter : " + param + ", New Value : " + value);
	}

	public void deleteCard(String cardId) {
		HttpResponse<String> response = Unirest.delete("https://api.trello.com/1/cards/" + cardId)
				.queryString("key", "954f7852f6b177e16d5a42118cf67e22")
				.queryString("token", "c68855e9adcfb1a01885056d7cb31a419d30b84dc72851e2b654efa12ef969b2").asString();

		response.getBody();
		logger.info("Card Deleted. Card ID : " + cardId);
	}

	public void deleteBoard(String boardId) {
		HttpResponse<String> response = Unirest.delete("https://api.trello.com/1/boards/" + boardId)
				.queryString("key", "954f7852f6b177e16d5a42118cf67e22")
				.queryString("token", "c68855e9adcfb1a01885056d7cb31a419d30b84dc72851e2b654efa12ef969b2").asString();
		response.getBody();

		logger.info("Board Deleted. Card ID : " + boardId);
	}

	private String getFirstIdFromResponse(String response) {
		String id = "";
		Matcher m = Pattern.compile("\\{\"id\":\"(.*?)\"").matcher(response);
		if (m.find()) {
			id = m.group(1);
		}
		return id;
	}

	private ArrayList<String> getAllIdFromResponse(String response) {
		ArrayList<String> idList = new ArrayList<String>();
		Matcher m = Pattern.compile("\\{\"id\":\"(.*?)\"").matcher(response);
		while (m.find()) {
			idList.add(m.group(1));
		}
		return idList;
	}

}
