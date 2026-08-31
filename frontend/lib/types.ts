export type Role = 'CANDIDATE' | 'COMPANY' | 'ADMIN';
export type ExperienceLevel = 'INTERN' | 'JUNIOR' | 'MID_LEVEL' | 'SENIOR';
export type EmploymentType = 'FULL_TIME' | 'PART_TIME' | 'CONTRACT' | 'INTERNSHIP';
export type JobStatus = 'OPEN' | 'CLOSED';
export type ApplicationStatus = 'APPLIED' | 'UNDER_REVIEW' | 'INTERVIEW' | 'REJECTED' | 'ACCEPTED';

export interface UserSummary { id: number; name: string; email: string; role: Role }
export interface AuthResponse { accessToken: string; tokenType: string; expiresIn: number; user: UserSummary }
export interface CompanySummary { id: number; companyName: string }
export interface Job {
  id: number; title: string; description: string; company: CompanySummary;
  city?: string; state?: string; country?: string; remote: boolean;
  employmentType: EmploymentType; experienceLevel: ExperienceLevel;
  salaryMin?: number; salaryMax?: number; status: JobStatus; skills: string[];
  createdAt: string; updatedAt: string; expiresAt: string;
}
export interface Page<T> { content: T[]; totalElements: number; totalPages: number; number: number; size: number; first: boolean; last: boolean }
export interface CandidateProfile {
  id: number; userId: number; name: string; email: string; headline?: string; bio?: string;
  city?: string; state?: string; country?: string; experienceLevel?: ExperienceLevel;
  linkedinUrl?: string; githubUrl?: string; portfolioUrl?: string; skills: string[];
  createdAt: string; updatedAt: string;
}
export interface CompanyProfile {
  id: number; userId: number; ownerName: string; email: string; companyName: string;
  description?: string; website?: string; city?: string; state?: string; country?: string;
  createdAt: string; updatedAt: string;
}
export interface CandidateSummary { id: number; name: string; headline?: string; experienceLevel?: ExperienceLevel; skills: string[] }
export interface Application { id: number; job: Job; candidate: CandidateSummary; status: ApplicationStatus; coverLetter?: string; appliedAt: string; updatedAt: string }
export interface Favorite { id: number; job: Job; createdAt: string }
export interface Match { jobId: number; candidateId: number; matchScore: number; matchedSkills: string[]; missingSkills: string[] }
export interface Recommended { job: Job; matchScore: number; matchedSkills: string[]; missingSkills: string[] }
export interface CandidateDashboard { totalApplications: number; underReview: number; interviews: number; rejected: number; accepted: number; favoriteJobs: number }
export interface CompanyDashboard { openJobs: number; closedJobs: number; totalApplications: number; underReview: number; interviews: number; accepted: number }
export interface JobFilters { title?: string; remote?: boolean; city?: string; state?: string; experienceLevel?: ExperienceLevel; employmentType?: EmploymentType; skill?: string; page?: number; size?: number }
export interface JobPayload { title: string; description: string; city?: string; state?: string; country?: string; remote: boolean; employmentType: EmploymentType; experienceLevel: ExperienceLevel; salaryMin?: number; salaryMax?: number; expiresAt: string; skills: string[] }
