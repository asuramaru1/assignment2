package tetris;

import static org.junit.Assert.*;

import org.junit.*;

public class BoardTest {
	Board b36 ;
	Board b88 ;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;
	private Piece[] pieces;
	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	@Before
	public void setUp() throws Exception {
		 pieces  = Piece.getPieces();
		b36 = new Board(3, 6);
		b88 = new Board(8 ,8);
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation(); 
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();

	}

	// Check the basic width/height/max after the one placement
	@Test
	public void testSample1() {
		assertEquals(0, b36.getColumnHeight(0));
		assertEquals(0, b36.getColumnHeight(1));
		assertEquals(0, b36.getMaxHeight());
		assertEquals(0, b36.getRowWidth(0));
		assertEquals(0, b36.getRowWidth(1));
		assertEquals(0, b36.getRowWidth(2));
	}
	
	// Place sRotated into the board, then check some measures

	@Test
	public void testPLace1(){
		b36.commit();
		assertEquals(0,b36.getMaxHeight());
		assertEquals(3,b36.getWidth());
		assertEquals(6,b36.getHeight());
		assertEquals(Board.PLACE_OK , b36.place(pieces[Piece.SQUARE] ,0,0));
		//assertEquals(Board.PLACE_OK , b36.place(pyr1 ,0,0));
		assertEquals(2, b36.getMaxHeight());
	}
	@Test
	public void testPLace2(){

		b36.commit();
		assertEquals(Board.PLACE_ROW_FILLED , b36.place(pieces[Piece.PYRAMID] ,0,0));
		b36.commit();
		//assertEquals(Board.PLACE_OK , b36.place(pyr1 ,0,0));
		assertEquals(2, b36.getMaxHeight());
		b36.clearRows();
		assertEquals(1,b36.getMaxHeight());
		b36.commit();
		assertEquals(Board.PLACE_BAD, b36.place(pieces[Piece.SQUARE],0,0));
		b36.undo();
		assertEquals(Board.PLACE_OK, b36.place(pieces[Piece.SQUARE],0,1));
		b36.commit();
		assertEquals(3 , b36.getMaxHeight());
		assertEquals(Board.PLACE_OUT_BOUNDS , b36.place(pieces[Piece.STICK].fastRotation(),1,1));
	}

	@Test
	public void testPLace3() {
		Board b55 = new Board(5,5);
		b55.commit();
		for(int i = 0 ; i<4 ; i++) {
			assertEquals(Board.PLACE_OK, b55.place(pieces[Piece.STICK].fastRotation(), 0, i));
			b55.commit();
		}
		assertEquals(4,b55.getMaxHeight());
		for(int i = 0 ; i<5 ; i++){
			assertEquals(i<4?4:0 , b55.getColumnHeight(i));
			assertEquals(i<4?4:0,b55.getRowWidth(i));
		}
		assertEquals(Board.PLACE_ROW_FILLED , b55.place(pieces[Piece.STICK] , 4,0));
		b55.commit();
		assertEquals(4 , b55.clearRows());
		b55.commit();
		assertEquals(0 , b55.getMaxHeight());
		for(int i = 0 ; i<5 ; i++){
			assertEquals(0 , b55.getColumnHeight(i));
			assertEquals(0,b55.getRowWidth(i));
		}
	}

	@Test
	public void Drop1() {
		assertEquals(0 , b36.dropHeight(pieces[Piece.SQUARE],0));
		assertEquals(Board.PLACE_OK , b36.place(pieces[Piece.SQUARE],0,0));
		b36.commit();
		assertEquals(2 , b36.dropHeight(pieces[Piece.SQUARE],0));
		assertEquals(2 , b36.dropHeight(pieces[Piece.SQUARE],1));
		assertEquals(0 , b36.dropHeight(pieces[Piece.STICK],2));

	}
	@Test
	public void Drop2() {
		Board b1010 = new Board(10,10);
		b1010.commit();
		assertEquals(Board.PLACE_OK , b1010.place(pieces[Piece.SQUARE],0,0));
		b1010.commit();
		assertEquals(Board.PLACE_OK , b1010.place(pieces[Piece.SQUARE],0,2));
		b1010.commit();
		assertEquals(Board.PLACE_OK , b1010.place(pieces[Piece.SQUARE],0,4 ));
		b1010.commit();
		assertEquals(Board.PLACE_OK , b1010.place(pieces[Piece.SQUARE],2,0));
		b1010.commit();
		assertEquals(Board.PLACE_OK , b1010.place(pieces[Piece.SQUARE],2,2));
		b1010.commit();
		assertEquals(Board.PLACE_OK , b1010.place(pieces[Piece.SQUARE],4,0));
		b1010.commit();
		for(int i = 0 ; i<6 ; i++){
			assertEquals(6-2*(i/2),b1010.dropHeight(pieces[Piece.SQUARE] , i));
		}
		String forCoverage = b1010.toString();
	}








}
	
	// Make  more tests, by putting together longer series of 
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.
	
