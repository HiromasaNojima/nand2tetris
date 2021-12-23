// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.

// init
@8192 // 256 * 32
D=A
@SCREEN
D=D+A
@SCREEN_MAX
M=D

(LOOP)
  @SCREEN
  D=A
  @SCREEN_ADDRESS
  M=D

  @KBD
  D=M

  // if any key is pressed jump to KEY_PRESSED, else to NO_KEY_PRESSED
  @KEY_PRESSED
  D;JNE
  @NO_KEY_PRESSED
  0;JMP

  // if key is pressed, color is black
  (KEY_PRESSED)
    @COLOR
    M=-1 // (1111111)
    @SCREEN_FILL
    0;JMP
    
  // if no key is pressed, color is white
  (NO_KEY_PRESSED)
    @COLOR
    M=0
    @SCREEN_FILL
    0;JMP

  (SCREEN_FILL)
    @COLOR
    D=M

    // change the color of SCREEN_ADDRESS
    @SCREEN_ADDRESS
    A=M
    M=D 

    // increment SCREEN_ADDRESS
    D=A+1
    @SCREEN_ADDRESS
    M=D

    @SCREEN_MAX
    D=M-D

    @SCREEN_FILL
    D;JNE

    @LOOP
    0;JMP

