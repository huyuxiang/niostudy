package daily.y2016.m08.d18.netty.example.securechat;

public class DelimiterBasedFrameDecoder extends FrameDecoder {

	private final ChannelBuffer[] delimiters;
	private final int maxFrameLength;
	private final boolean stripDelimiter;
	private final boolean failFast;
	private boolean discardingTooLongFrame;
	private int tooLongFrameLength;
	
	
	public DelimiterBasedFrameDecoder(int maxFrameLength, ChannelBuffer delimiter) {
		this(maxFrameLength, true, delimiter);
	}
	
	public DelimiterBasedFrameDecoder(
			int maxFrameLength, boolean stripDelimiter, ChannelBuffer delimiter) {
		this(maxFrameLength, stripDelimiter, false, delimiter);
	}
	
	public DelimiterBasedFrameDecoder(
			int maxFrameLength, boolean stripDelimiter, boolean failFast, 
			ChannelBuffer delimiter) {
		validateMaxFrameLength(maxFrameLength);
		validateDelimiter(delimiter);
		delimiters = new ChannelBuffer[] {
				delimiter.slice(
						delimiter.readerIndex(), delimiter.readableBytes())
		};
		this.maxFrameLength = maxFrameLength;
		this.stripDelimiter = stripDelimiter;
		this.failFast = failFast;
	}
	
	public DelimiterBasedFrameDecoder(int maxFrameLength, ChannelBuffer... delimiters) {
		this(maxFrameLength, true, delimiters);
	}
	
	
	public DelimiterBasedFrameDecoder(
			int maxFrameLength, boolean stripDelimiter, ChannelBuffer... delimiters) {
		this(maxFrameLength, stripDelimiter, false, delimiters);
	}
	
	@Override
	protected Object decode(
			ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		int minFrameLength = Integer.MAX_VALUE;
		ChannelBuffer minDelim = null;
		for(ChannelBuffer delim: delimiters) {
			int frameLength = indexOf(buffer, delim);
			if(frameLength>=0&& frameLength<minFrameLength) {
				minFrameLength = frameLength;
				minDelim = delim;
			}
		}
		
		if(minDelim!=null) {
			int minDelimLength = minDelim.capacity();
			ChannelBuffer frame;
			
			if(discardingTooLongFrame) {
				discardingTooLongFrame = false;
				
			}
		}
	}
}
