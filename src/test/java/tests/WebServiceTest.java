/**
 * 
 */
package tests;

import java.util.ArrayList;
import java.util.Random;

import org.junit.jupiter.api.Test;

import pages.TrelloPage;

/**
 * @author can.terzi
 *
 */
public class WebServiceTest {

	private TrelloPage tp;
	
	@Test
	public void trelloTest() {
		tp = new TrelloPage();
		ArrayList<String> boards = new ArrayList<String>();
		ArrayList<String> lists = new ArrayList<String>();
		ArrayList<String> cards = new ArrayList<String>();

		// Create Board
		String boardId = tp.createBoard("Test Board");
		boards.add(boardId);

		// Create New List
		String listId = tp.createList("Test List", boardId);
		lists.add(listId);

		// Get All Lists' Id for New Board
		lists = tp.getLists(boardId);

		// Choose Random List
		Random random = new Random();
		int k = random.nextInt(lists.size());
		listId = lists.get(k);
		
		// Check cards size if its 0
		tp.checkCardAmount(listId, 0);

		// Create First Card For List
		String cardId = tp.createCard("Test Card", listId);
		cards.add(cardId);
		
		// Check cards size if its 1
		tp.checkCardAmount(listId, 1);

		// Create Second Card For List
		cardId = tp.createCard("Test Card 2", listId);
		cards.add(cardId);
		
		// Check cards size if its 2
		tp.checkCardAmount(listId, 2);

		// Choose Random Card
		random = new Random();
		int i = random.nextInt(cards.size());
		cardId = cards.get(i);

		// Update Card Name and Description then check if its successful
		tp.updateCard(cardId, "name", "NewName");
		tp.checkCardValue(cardId, "name", "NewName");

		tp.updateCard(cardId, "desc", "New Desc");
		tp.checkCardValue(cardId, "desc", "New Desc");

		// Delete All Created Cards
		for (String id : cards) {
			tp.deleteCard(id);
		}

		// Delete All Created Boards
		for (String id : boards) {
			tp.deleteBoard(id);
		}
	}
}
