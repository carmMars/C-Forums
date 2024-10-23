// src/app/components/register/register.component.ts

import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators,
  FormGroup,
} from '@angular/forms';
import { AuthService, UserDTO } from '../../services/auth.service'; // Ensure UserDTO is imported
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    RouterLink,
  ],
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  hide = true;
  successMessage: string | null = null; // Add this line
  errorMessage: string | null = null;   // Optional: To handle registration errors

  constructor(private fb: FormBuilder, private authService: AuthService) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

  onSubmit() {
    if (this.registerForm.valid) {
      const user: UserDTO = this.registerForm.value;
      this.authService.register(user).subscribe({
        next: (response) => {
          console.log('User registered successfully');
          this.successMessage = 'A verification email has been sent to your email. Please verify to continue your registration process.';
          this.errorMessage = null;

          this.registerForm.patchValue({ password: '' });
        },
        error: (error) => {
          console.error('Error registering user:', error);
          this.errorMessage = 'Registration failed. Please try again.';
          this.successMessage = null; // Reset success message if any
        },
      });
    }
  }
}
