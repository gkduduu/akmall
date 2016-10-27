//
//  TwitterLoginPopup.h
//
//  Created by Jaanus Kase on 15.01.10.
//  Copyright 2010. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "OAuthTwitterCallbacks.h"
#import "TwitterLoginPopupDelegate.h"
#import "TwitterLoginUiFeedback.h"

@class OAuth, TwitterWebViewController;

@interface TwitterLoginPopup : UIViewController <OAuthTwitterCallbacks,
    UIWebViewDelegate> {

    
	id <TwitterLoginPopupDelegate> delegate;
	id <TwitterLoginUiFeedback> uiDelegate;
	
	
	NSOperationQueue *queue;
	OAuth *oAuth;

	TwitterWebViewController *webViewController;

     
    UIWebView *webView;
}
@property (nonatomic, retain) IBOutlet UIWebView *webView;

@property (assign) id<TwitterLoginPopupDelegate> delegate;
@property (assign) id<TwitterLoginUiFeedback> uiDelegate;

@property (nonatomic, retain) OAuth *oAuth;

- (IBAction)btnCancel:(id)sender;

@end


