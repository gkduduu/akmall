//
//  TouchScrollView.m
//  AppAkPlaza_IOS
//
//  Created by 김 두민 on 12. 6. 7..
//  Copyright (c) 2012년 puuu80@uniwis.com. All rights reserved.
//

#import "TouchScrollView.h"

@implementation TouchScrollView

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    return self;
}

- (void)didReceiveMemoryWarning
{
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event{
    [self.superview touchesBegan:touches withEvent:event];
}
- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event{
    [self.superview touchesEnded:touches withEvent:event];
}
- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event{
    [self.superview touchesMoved:touches withEvent:event];
}
- (void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event{
    [self.superview touchesCancelled:touches withEvent:event];
}

- (void)viewDidUnload
{
    
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end 