//
//  AkMainModel.h
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 22..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "baseModel.h"
#import "CommonURL.h"
//#import "IMTWebView.h"

@interface AkMainModel : baseModel 

{
    NSString *sendURL;
}


- (void)procVersion;
- (UIWebView *)mainAkPlazaWebView;
- (UIWebView *)mainSubWebView:(NSString*)strURL;


@end
