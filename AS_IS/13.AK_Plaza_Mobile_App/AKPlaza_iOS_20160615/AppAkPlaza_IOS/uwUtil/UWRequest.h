//
//  UWRequest.h
//  AppAkMall_iOS
//
//  Created by uniwis on 11. 8. 24..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AkLoginModel.h"
#import "OLogin.h"
#import "OLogin+UserDefaults.h"
#import "CommonMsg.h"




@interface UWRequest : NSObject <UIAlertViewDelegate> {
    NSString *storedUserID;
}

+ (NSString *)createQueryString:(NSDictionary *)dictParam;
+ (NSMutableURLRequest *)urlRequestParam:(NSString *)queryString andWithURL:(NSString *)strURL;
- (BOOL) commonShouldStartLoadWithRequest:(NSURLRequest *)request withObject:(id)object;





@end
