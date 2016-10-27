//
//  AkLoginModel.h
//  AppAkPlaza_IOS
//
//  Created by  on 12. 1. 3..
//  Copyright (c) 2012년 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "baseModel.h"
#import "ASIFormDataRequest.h"
#import "JSON.h"
#import "OLogin.h"
#import "OLogin+UserDefaults.h"
#import "CommonURL.h"
#import "AlertMassageUtil.h"

@interface AkLoginModel : baseModel <ASIHTTPRequestDelegate>


@property (nonatomic, retain) OLogin* oLogin;


- (void)procLogin:(OLogin*)objLogin;
- (void)procLogout;
- (void)procAutoLogin:(OLogin*)objLogin;

@end
