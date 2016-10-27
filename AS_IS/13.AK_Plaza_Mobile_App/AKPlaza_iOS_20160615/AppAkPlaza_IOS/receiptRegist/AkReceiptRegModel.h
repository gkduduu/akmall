//
//  AkReceiptRegModel.h
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 28..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "baseModel.h"
#import "ASIFormDataRequest.h"
#import "JSON.h"
#import "CommonURL.h"
#import "AlertMassageUtil.h"

@interface AkReceiptRegModel : baseModel <ASIHTTPRequestDelegate>



- (BOOL)receiptReg:(NSString*)barcodenumAndAmount;

@end
