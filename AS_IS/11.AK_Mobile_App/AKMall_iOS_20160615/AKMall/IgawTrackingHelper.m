//
//  IgawTrackingHelper.m
//  AKMall
//
//  Created by UNIWIS-MAC on 2015. 10. 20..
//  Copyright (c) 2015년 한병일. All rights reserved.
//

#import "IgawTrackingHelper.h"
#import <IgaworksCommerce/IgaworksCommerce.h>

@implementation IgawTrackingHelper

static NSString *lastPurchaseJson = @"";
+ (BOOL)trackingPurchase:(UIWebView*)targetWebview
{
    NSString *purchaseJsonString = [targetWebview stringByEvaluatingJavaScriptFromString:@"purchaseJson"];
    // 중복호출 방지
    if([purchaseJsonString isEqualToString:lastPurchaseJson]){
        return NO;
    }

    if(purchaseJsonString && ![purchaseJsonString isEqualToString:@""]){
//        NSLog(@"purchaseJsonString %@",purchaseJsonString);
        lastPurchaseJson = purchaseJsonString;
        // IGAW연동
        [IgaworksCommerce purchase:purchaseJsonString];
        return YES;
    }else{
        lastPurchaseJson = @"";
        return NO;
    }
}


@end
