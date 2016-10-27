//
//  AkReceiptRegModel.m
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 28..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import "AkReceiptRegModel.h"

@implementation AkReceiptRegModel

- (void)procReceiptRegResult:(ASIFormDataRequest *) request
{
    // Use when fetching text data
    NSString *responseString = [request responseString];
    DLog(@"Got responseString Login INFO: %@", responseString);
    
    NSMutableDictionary *responseJSON = [responseString JSONValue];   
    

    NSString *strResult   = [responseJSON objectForKey:@"result"];
    
    MSG(nil, @"확인", strResult);
    
    [self activityStop]; 
}


- (BOOL)receiptReg:(NSString*)barcodenumAndAmount
{
    
    [self activityStart]; 
    
    NSURL *url = [NSURL URLWithString:kLibURL];
    
    NSArray *arr = [barcodenumAndAmount componentsSeparatedByString:@","];
    
    
    
    NSString *barcodenum = [arr objectAtIndex:0];
    NSString *amount  = [arr objectAtIndex:1];
    
    
    ASIFormDataRequest *request = [ASIFormDataRequest requestWithURL:url];
    
    [request setDelegate:self];
    
    [request setRequestMethod:@"POST"];
    
    //applyCookieHeader : 여기서 jsseionid를 알아서 넣어준다 

    [request setPostValue:@"appleReceiptRegProc"    forKey:@"act"];
    [request setPostValue:barcodenum    forKey:@"recptNo"];
    [request setPostValue:amount                   forKey:@"recptPay"];
    
    [request setDidFinishSelector:@selector(procReceiptRegResult:)];
    
    [request startSynchronous];
    
    if ([request error])
    {
        MSG(nil, @"확인", @"영수증 등록 요청 실패");
        [self activityStop]; 
    }

    
    
    return YES;
    
}




@end
