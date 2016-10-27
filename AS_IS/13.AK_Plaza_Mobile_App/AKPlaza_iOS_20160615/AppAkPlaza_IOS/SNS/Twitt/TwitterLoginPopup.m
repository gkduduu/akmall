//
//  TwitterLoginPopup.m
//
//  Created by Jaanus Kase on 15.01.10.
//  Copyright 2010. All rights reserved.
//

#import "TwitterLoginPopup.h"
#import "OAuth.h"


@implementation TwitterLoginPopup
@synthesize webView;

@synthesize delegate, uiDelegate, oAuth;


#pragma mark -
#pragma mark OAuthTwitterCallbacks protocol

// For all of these methods, we invoked oAuth in a background thread, so these are also called
// in background thread. So we first transfer the control back to main thread before doing
// anything else.

- (void) requestTwitterTokenDidSucceed:(OAuth *)_oAuth {
	if (![NSThread isMainThread]) {
		[self performSelectorOnMainThread:@selector(requestTwitterTokenDidSucceed:)
							   withObject:_oAuth
							waitUntilDone:NO];
		return;
	}
    
    DLog(@"%@" , _oAuth.oauth_token);

	NSURL *myURL = [NSURL URLWithString:[NSString
										 stringWithFormat:@"https://api.twitter.com/oauth/authorize?oauth_token=%@",
										 _oAuth.oauth_token]];
	


    
	[self.webView loadRequest:[NSURLRequest requestWithURL:myURL]];
    self.webView.delegate = self;
    self.webView.scalesPageToFit = YES;
    
    
	[self.uiDelegate tokenRequestDidSucceed:self];

}



- (void) requestTwitterTokenDidFail:(OAuth *)_oAuth {
	if (![NSThread isMainThread]) {
		[self performSelectorOnMainThread:@selector(requestTwitterTokenDidFail:)
							   withObject:_oAuth
							waitUntilDone:NO];
		return;
	}


    
	[self.uiDelegate tokenRequestDidFail:self];
	
}

- (void) authorizeTwitterTokenDidSucceed:(OAuth *)_oAuth {
	if (![NSThread isMainThread]) {
		[self performSelectorOnMainThread:@selector(authorizeTwitterTokenDidSucceed:)
							   withObject:_oAuth
							waitUntilDone:NO];
		return;
	}

	[self.uiDelegate authorizationRequestDidSucceed:self];
    //로그인 성공하면 로그인 유지하려고 유저 정보 저장 
    [self.delegate twitterLoginPopupDidAuthorize:self];
}

- (void) authorizeTwitterTokenDidFail:(OAuth *)_oAuth {
	if (![NSThread isMainThread]) {
		[self performSelectorOnMainThread:@selector(authorizeTwitterTokenDidFail:)
							   withObject:_oAuth
							waitUntilDone:NO];
		return;
	}
    
	[self.uiDelegate authorizationRequestDidFail:self];	
}

#pragma mark -
#pragma mark UIViewController and memory mgmt


// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
    [super viewDidLoad];
    
    queue = nil;
	queue = [[NSOperationQueue alloc] init];
    
    [self.uiDelegate tokenRequestDidStart:self];
	
	
	NSInvocationOperation *operation = [[NSInvocationOperation alloc]
										initWithTarget:oAuth
										selector:@selector(synchronousRequestTwitterToken)
										object:nil];
	
	[queue addOperation:operation];
	[operation release];
    operation = nil;
    
	
	oAuth.delegate = self;
  
    
}

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
    
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
    
    [self setOAuth:nil];
    [self setWebView:nil];
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
	[queue release];
    queue = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
 // Return YES for supported orientations
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
}





- (void)dealloc {
    

	
	[webView release];

	[oAuth release];

    [super dealloc];
}

#pragma mark -
#pragma mark UIWebViewDelegate

- (void)webViewDidStartLoad:(UIWebView *)webView {
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:YES];
}

- (void)webViewDidFinishLoad:(UIWebView *)webView {
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];


    [self.uiDelegate authorizationRequestDidStart:self];
    
    NSInvocationOperation *operation = [[NSInvocationOperation alloc]
                                        initWithTarget:oAuth
                                        selector:@selector(synchronousAuthorizeTwitterTokenWithVerifier:)
                                        object:nil];
    [queue addOperation:operation];
    [operation release];
    operation = nil;
    

}

- (IBAction)btnCancel:(id)sender {
    
    [[UIApplication sharedApplication] setNetworkActivityIndicatorVisible:NO];
    
    [self.delegate twitterLoginPopupDidCancel:self];
    
}
@end
