//
//  DataFormatter.m
//  DietDiary
//
//  Created by taehoon koo on 12/6/10.
//  Copyright 2010 NHN I&S. All rights reserved.
//

#import "DataFormatter.h"


@implementation DataFormatter

+(NSString*)moneyFormat:(NSString*)strNumber{
    
    //스트링을 INT로 변경
	strNumber = [strNumber stringByReplacingOccurrencesOfString:@"," withString:@""];
    int nTmp = [strNumber intValue];
    
    NSNumberFormatter *fmt = [[[NSNumberFormatter alloc] init]autorelease];
    [fmt setNumberStyle:NSNumberFormatterDecimalStyle];
    
    //NSString 으로 저장
    NSString *formatedString = [fmt stringFromNumber:[NSNumber numberWithInt:nTmp]];
    
    return formatedString;
}

@end
