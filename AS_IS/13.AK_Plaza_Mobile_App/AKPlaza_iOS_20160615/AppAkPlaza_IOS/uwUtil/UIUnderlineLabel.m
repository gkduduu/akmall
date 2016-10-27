//
//  UIStrikeLabel.m
//  StrikedLabelSample
//
//  Created by Min-gu, Kim on 10. 9. 16..
//  Copyright 2010 더블가이. All rights reserved.
//

#import "UIUnderlineLabel.h"
#define underlineHeight 1.0


@interface UIUnderlineLabel(UILabel)

- (CGRect) getUnderlineRect:(CGFloat)underlineWidth;

@end



@implementation UIUnderlineLabel
@synthesize underline;

- (void) drawTextInRect:(CGRect)rect {
	[super drawTextInRect:rect];
	
	CGContextRef context = UIGraphicsGetCurrentContext();
	
	if([self underline]) {
		CGSize textSize = [[self text] sizeWithFont:[self font]];	
		CGFloat underlineWidth = textSize.width;	
		CGContextFillRect(context, [self getUnderlineRect:underlineWidth]);
	}
}

- (CGRect)getUnderlineRect:(CGFloat)underlineWidth {
	
	CGRect underlineRect;
	CGFloat x;
	
    CGFloat y = (self.frame.size.height - underlineHeight) / 1.0;
	
    
	switch ([self textAlignment]) {
		default:
		case UITextAlignmentLeft:
			x = 0;
			break;
		case UITextAlignmentCenter:
			x = (self.frame.size.width - underlineWidth) / 2.0;
			break;
		case UITextAlignmentRight:
			x = self.frame.size.width - underlineWidth;
			break;
	}
	
	underlineRect = CGRectMake(x, y, underlineWidth, underlineHeight);
	
	return underlineRect;
}

@end
