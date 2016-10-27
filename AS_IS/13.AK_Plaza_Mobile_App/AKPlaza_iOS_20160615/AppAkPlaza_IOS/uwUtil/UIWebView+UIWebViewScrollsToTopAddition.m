/*
 UIWebView+ScrollsToTop.m
 http://github.com/petejkim/useful-ios-tidbits
 
 Licensed under the MIT License
 Copyright (C) 2011 by Peter Jihoon Kim
 
 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:
 
 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */


#import "UIWebView+UIWebViewScrollsToTopAddition.h"

@implementation UIWebView (UIWebViewScrollsToTopAddition)


@dynamic scrollsToTop;

- (BOOL)scrollsToTop {
    for(UIView *view in self.subviews) {
        if([view isKindOfClass:[UIScrollView class]]) {
            return ((UIScrollView *)view).scrollsToTop;
        }
    }
    return NO;
}

- (void)setScrollsToTop:(BOOL)scrollsToTop {
    for(UIView *view in self.subviews) {
        if([view isKindOfClass:[UIScrollView class]]) {
            ((UIScrollView *)view).scrollsToTop = scrollsToTop;
            return;
        }
    }
}


@end
