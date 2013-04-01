package net.nightwhistler.pageturner.view.bookview;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import android.text.StaticLayout;

public class LayoutTextUtil {

	/**
	 * Very simple Layout class with predictable properties.
	 * 
	 * It ignores all formatting, creating fixed-length lines.
	 * 
	 * @author Alex Kuiper
	 *
	 */
	private static class SimpleLayout {
		private CharSequence text;
		private int lineWidth;
		private int lineHeight;
		
		public SimpleLayout(CharSequence text, int lineWidth, int lineHeight) {
			this.text = text;
			this.lineWidth = lineWidth;
			this.lineHeight = lineHeight;
		}
		
		public int getLineCount() {
			return (text.length() / lineWidth) + 1;
		}
		
		public int getHeight() {
			return getLineCount() * lineHeight;
		}
		
		public int getLineForVertical(int vertical) {
			
			int line = vertical / lineHeight;
			int lastLine = text.length() / lineWidth;
			
			if ( line < 0 ) {
				return 0;
			} else if ( line > lastLine ) {
				return lastLine;
			}
			
			return line;
		}
		
		public int getLineStart(int line) {
			
			if ( line >= getLineCount() ) {
				return text.length();
			}
			
			return line * lineWidth;
		}
		
		public int getLineEnd(int line) {
			return getLineStart(line + 1);
		}
		
		public int getLineTop( int line ) {
			return line * lineHeight;
		}
		
		public int getLineForOffset( int offset ) {
			return offset / lineWidth;
		}
	}
	
	
	/**
	 * Returns a StaticLayout which delegates most work to a SimpleLayout.
	 * 
	 * @param text the text to layout
	 * @param lineWidth
	 * @param lineHeight
	 * @return
	 */
	public static StaticLayout createMockLayout( final CharSequence text, final int lineWidth, final int lineHeight ) {
		
		final StaticLayout mockLayout = mock(StaticLayout.class);
		final SimpleLayout simpleLayout = new SimpleLayout(text, lineWidth, lineHeight);
		
		when( mockLayout.getLineCount() ).thenReturn( simpleLayout.getLineCount() );
		when( mockLayout.getHeight() ).thenReturn( simpleLayout.getHeight() );	
		
		
		when( mockLayout.getLineForVertical(anyInt())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(InvocationOnMock invocation) throws Throwable {
				Integer vertical = (Integer) invocation.getArguments()[0];
				return simpleLayout.getLineForVertical(vertical);
			}
		});		
		
		when( mockLayout.getLineStart(anyInt())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(InvocationOnMock invocation) throws Throwable {
				Integer line = (Integer) invocation.getArguments()[0];
				return simpleLayout.getLineStart(line);
			}
		});
		
		when( mockLayout.getLineEnd(anyInt())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(InvocationOnMock invocation) throws Throwable {
				int line = (Integer) invocation.getArguments()[0];
				return simpleLayout.getLineEnd(line);
			}
		});
		
		when( mockLayout.getLineForOffset(anyInt())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(InvocationOnMock invocation) throws Throwable {
				int offset = (Integer) invocation.getArguments()[0];
				return simpleLayout.getLineForOffset(offset);
			}
		});
		
		when( mockLayout.getLineTop(anyInt())).thenAnswer(new Answer<Integer>() {
			@Override
			public Integer answer(InvocationOnMock invocation) throws Throwable {
				int line = (Integer) invocation.getArguments()[0];
				return simpleLayout.getLineTop(line);
			}
		});
		
		
		return mockLayout;
	}
	
	public static String getStringOfLength( String seed, int length ) {
		StringBuffer buffer = new StringBuffer();
		
		int repeat = length / seed.length();
		
		for ( int i=0; i < repeat; i++ ) {
			buffer.append(seed);
		}
		
		int remainder = length % seed.length();
		
		buffer.append( seed.substring(0, remainder) );
		String result = buffer.toString();
		Assert.assertEquals( length, result.length());
		
		return result;
	}
	
	
}
