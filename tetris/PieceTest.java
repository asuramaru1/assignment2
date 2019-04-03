package tetris;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated;
	Piece ar[] ;

	@Before
	public void setUp() throws Exception {
		ar = Piece.getPieces();
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
	}
	
	// Here are some sample tests to get you started
	
	@Test
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
	}
	@Test
    public void testSampleRotation(){
	    assertTrue(pyr2.equals(pyr1.computeNextRotation()));
        assertTrue(pyr3.equals(pyr2.computeNextRotation()));
       assertTrue(pyr4.equals(pyr3.computeNextRotation()));
        assertTrue(pyr1.equals(pyr4.computeNextRotation()));
    }
    @Test
	public void testFastRotationsPyramid(){
		Piece pyr =  ar[Piece.PYRAMID];
		assertTrue(pyr2.equals(pyr.fastRotation()));
		assertTrue(pyr3.equals(pyr.fastRotation().fastRotation()));
		assertTrue(pyr4.equals(pyr.fastRotation().fastRotation().fastRotation()));
		assertTrue(pyr1.equals(pyr.fastRotation().fastRotation().fastRotation().fastRotation()));
	}
	@Test
	public void testSkirt(){
		assertTrue(Arrays.equals(new int[] {0,0} , ar[Piece.L1].getSkirt()));
		assertTrue(Arrays.equals(new int[] {0,0,0} , ar[Piece.L1].fastRotation().getSkirt()));

		assertTrue(Arrays.equals(new int[] {0,0} , ar[Piece.L2].getSkirt()));
		assertTrue(Arrays.equals(new int[] {1,1,0} , ar[Piece.L2].fastRotation().getSkirt()));

		assertTrue(Arrays.equals(new int[] {0,0} , ar[Piece.SQUARE].getSkirt()));
		assertTrue(Arrays.equals(new int[] {0,0} , ar[Piece.SQUARE].fastRotation().getSkirt()));


		assertTrue(Arrays.equals(new int[] {0} , ar[Piece.STICK].getSkirt()));
		assertTrue(Arrays.equals(new int[] {0,0,0,0} , ar[Piece.STICK].fastRotation().getSkirt()));

		assertTrue(Arrays.equals(new int[] {0,0,1} , ar[Piece.S1].getSkirt()));
		assertTrue(Arrays.equals(new int[] {1,0} , ar[Piece.S1].fastRotation().getSkirt()));

		assertTrue(Arrays.equals(new int[] {1,0,0} , ar[Piece.S2].getSkirt()));
		assertTrue(Arrays.equals(new int[] {0,1} , ar[Piece.S2].fastRotation().getSkirt()));


	}
	@Test
	public void testDistinctRotations(){
		assertTrue(ar[Piece.SQUARE].equals((ar[Piece.SQUARE].fastRotation())));
		assertTrue(ar[Piece.S1].equals((ar[Piece.S1].fastRotation().fastRotation())));
		assertTrue(ar[Piece.S2].equals((ar[Piece.S2].fastRotation().fastRotation())));

		assertTrue(!ar[Piece.L1].equals((ar[Piece.L1].fastRotation().fastRotation())));
		assertTrue(ar[Piece.L1].equals((ar[Piece.L1].fastRotation().fastRotation().fastRotation().fastRotation())));

		assertTrue(ar[Piece.STICK].equals((ar[Piece.STICK].fastRotation().fastRotation())));

	}
	@Test
	public void testWidthHeight(){
		assertEquals(ar[Piece.STICK].getWidth() ,ar[Piece.STICK].fastRotation().getHeight() );
		assertEquals(ar[Piece.STICK].getHeight() ,ar[Piece.STICK].fastRotation().getWidth() );

		assertEquals(ar[Piece.L1].getWidth() ,ar[Piece.L1].fastRotation().getHeight() );
		assertEquals(ar[Piece.L1].getHeight() ,ar[Piece.L1].fastRotation().getWidth() );

		assertEquals(ar[Piece.S1].getWidth() ,ar[Piece.S1].fastRotation().getHeight() );
		assertEquals(ar[Piece.S1].getHeight() ,ar[Piece.S1].fastRotation().getWidth() );

		assertEquals(ar[Piece.SQUARE].getWidth() ,ar[Piece.SQUARE].fastRotation().getHeight() );
		assertEquals(ar[Piece.SQUARE].getHeight() ,ar[Piece.SQUARE].fastRotation().getWidth() );


	}
	
	// Test the skirt returned by a few pieces
	@Test
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
	}
	
	
}
