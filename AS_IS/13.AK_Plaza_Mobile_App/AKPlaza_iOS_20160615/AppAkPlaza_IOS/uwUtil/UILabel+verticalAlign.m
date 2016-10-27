//
//  UILabel+verticalAlign.m
//  AppAkPlaza_IOS
//
//  Created by  on 12. 1. 18..
//  Copyright (c) 2012년 __MyCompanyName__. All rights reserved.
//

#import "UILabel+verticalAlign.h"

@implementation UILabel  (verticalAlign)



-(void)setUILabel:(UILabel *)label verticalAlign:(int)vAlign
{
    //지정한 사이즈(높이)보다 내용이 적을 경우 중앙정렬이 되어버린다 
    //해당 함수를 사용하면 원하는 valign을 설정할수 있다 
    
    CGSize textSize = [label.text sizeWithFont:label.font constrainedToSize:label.frame.size lineBreakMode:label.lineBreakMode];
    
    
    switch (vAlign) {
        case 0:  //top
            label.frame = CGRectMake(label.frame.origin.x, label.frame.origin.y, label.frame.size.width, textSize.height);
            
            break;
            
        case 1:  //bottom
            label.frame = CGRectMake(label.frame.origin.x, (label.frame.origin.y + label.frame.size.height) - textSize.height, label.frame.size.width, textSize.height);
            break;
    }
    
}





@end
